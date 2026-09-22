package com.cartit.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cartit.dto.response.OrderResponse;
import com.cartit.entity.Order;
import com.cartit.entity.User;
import com.cartit.enums.OrderStatus;
import com.cartit.exception.BadRequestException;
import com.cartit.exception.ResourceNotFoundException;
import com.cartit.repository.OrderRepository;
import com.cartit.repository.UserRepository;
import com.cartit.security.CurrentUserService;
import com.cartit.service.DeliveryService;
import com.cartit.service.builder.OrderResponseBuilder;

@Service
@Transactional
public class DeliveryServiceImpl implements DeliveryService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;
    private final OrderResponseBuilder orderResponseBuilder;

    public DeliveryServiceImpl(OrderRepository orderRepository,
                               UserRepository userRepository,
                               CurrentUserService currentUserService,
                               OrderResponseBuilder orderResponseBuilder) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
        this.orderResponseBuilder = orderResponseBuilder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllocatedOrders() {
        User currentUser = currentUserService.getCurrentUser();
        List<Order> orders = orderRepository.findByDeliveryBoyIdAndActiveTrueOrderByCreatedAtDesc(currentUser.getId());

        if (orders.isEmpty()) {
            // Also include unassigned active orders near store
            List<OrderStatus> terminalStatuses = List.of(OrderStatus.DELIVERED, OrderStatus.CANCELLED);
            orders = orderRepository.findByDeliveryBoyIsNullAndOrderStatusNotInAndActiveTrue(terminalStatuses);
        }

        return orderResponseBuilder.build(orders);
    }

    @Override
    public void updateLocation(Double latitude, Double longitude) {
        if (latitude == null || longitude == null) return;

        User currentUser = currentUserService.getCurrentUser();
        currentUser.setLatitude(latitude);
        currentUser.setLongitude(longitude);
        userRepository.save(currentUser);

        // Update current delivery coordinates on active order assigned to this driver
        List<OrderStatus> activeStatuses = List.of(
            OrderStatus.CONFIRMED,
            OrderStatus.PACKED,
            OrderStatus.ARRIVED_AT_STORE,
            OrderStatus.OUT_FOR_DELIVERY,
            OrderStatus.ARRIVED_AT_CUSTOMER
        );
        List<Order> activeOrders = orderRepository.findByDeliveryBoyIdAndOrderStatusNotInAndActiveTrue(
            currentUser.getId(),
            List.of(OrderStatus.DELIVERED, OrderStatus.CANCELLED)
        );

        for (Order order : activeOrders) {
            order.setCurrentDeliveryLatitude(latitude);
            order.setCurrentDeliveryLongitude(longitude);
            orderRepository.save(order);
        }
    }

    @Override
    public OrderResponse updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findByIdAndActiveTrue(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        order.setOrderStatus(status);

        if (status == OrderStatus.DELIVERED || status == OrderStatus.CANCELLED) {
            User deliveryBoy = order.getDeliveryBoy();
            if (deliveryBoy != null) {
                deliveryBoy.setIsAvailable(true);
                userRepository.save(deliveryBoy);
            }
        }

        Order saved = orderRepository.save(order);
        return orderResponseBuilder.build(saved);
    }

    @Override
    public OrderResponse acceptOrder(Long orderId) {
        User currentUser = currentUserService.getCurrentUser();
        Order order = orderRepository.findByIdAndActiveTrue(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getDeliveryBoy() != null && !order.getDeliveryBoy().getId().equals(currentUser.getId())) {
            throw new BadRequestException("Order is already assigned to another delivery partner");
        }

        order.setDeliveryBoy(currentUser);
        if (currentUser.getLatitude() != null && currentUser.getLongitude() != null) {
            order.setCurrentDeliveryLatitude(currentUser.getLatitude());
            order.setCurrentDeliveryLongitude(currentUser.getLongitude());
        }
        currentUser.setIsAvailable(false);
        userRepository.save(currentUser);

        Order saved = orderRepository.save(order);
        return orderResponseBuilder.build(saved);
    }
}
