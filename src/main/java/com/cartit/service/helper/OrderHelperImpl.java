package com.cartit.service.helper;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cartit.entity.Order;
import com.cartit.entity.OrderItem;
import com.cartit.entity.User;
import com.cartit.exception.ResourceNotFoundException;
import com.cartit.repository.OrderItemRepository;
import com.cartit.repository.OrderRepository;
import com.cartit.security.CurrentUserService;

@Service
public class OrderHelperImpl implements OrderHelper {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CurrentUserService currentUserService;

    public OrderHelperImpl(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            CurrentUserService currentUserService) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.currentUserService = currentUserService;
    }

    @Override
    public Order getOrder(Long orderId) {

        User user = currentUserService.getCurrentUser();

        return orderRepository
                .findByIdAndUserIdAndActiveTrue(
                        orderId,
                        user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found"));
    }

    @Override
    public Order getOrder(String orderNumber) {

        Order order = orderRepository
                .findByOrderNumber(orderNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found"));

        User user = currentUserService.getCurrentUser();

        if (!order.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Order not found");
        }

        return order;
    }

    @Override
    public List<Order> getMyOrders() {

        User user = currentUserService.getCurrentUser();

        return orderRepository
                .findByUserIdAndActiveTrueOrderByCreatedAtDesc(
                        user.getId());
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public OrderItem save(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    @Override
    public List<OrderItem> saveAllOrderItems(
            List<OrderItem> orderItems) {

        return orderItemRepository.saveAll(orderItems);
    }
}