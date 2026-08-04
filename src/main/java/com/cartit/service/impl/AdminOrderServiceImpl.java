package com.cartit.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cartit.dto.response.OrderResponse;
import com.cartit.entity.Order;
import com.cartit.enums.OrderStatus;
import com.cartit.service.AdminOrderService;
import com.cartit.service.InventoryService;
import com.cartit.service.builder.OrderResponseBuilder;
import com.cartit.service.helper.AdminOrderHelper;
import com.cartit.service.validator.OrderValidator;

@Service
@Transactional
public class AdminOrderServiceImpl implements AdminOrderService {

    private final AdminOrderHelper adminOrderHelper;
    private final OrderValidator orderValidator;
    private final OrderResponseBuilder orderResponseBuilder;
    private final InventoryService inventoryService;

    public AdminOrderServiceImpl(
            AdminOrderHelper adminOrderHelper,
            OrderValidator orderValidator,
            OrderResponseBuilder orderResponseBuilder,
            InventoryService inventoryService) {

        this.adminOrderHelper = adminOrderHelper;
        this.orderValidator = orderValidator;
        this.orderResponseBuilder = orderResponseBuilder;
        this.inventoryService = inventoryService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {

        return orderResponseBuilder.build(
                adminOrderHelper.getAllOrders());
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long orderId) {

        return orderResponseBuilder.build(
                adminOrderHelper.getOrder(orderId));
    }

    private OrderResponse updateOrderStatus(
            Long orderId,
            OrderStatus newStatus) {

        Order order = adminOrderHelper.getOrder(orderId);

        orderValidator.validateStatusTransition(
                order.getOrderStatus(),
                newStatus);

        if (newStatus == OrderStatus.CANCELLED) {

            orderValidator.validateCancellation(order);

            inventoryService.restoreStock(order);
        }

        order.setOrderStatus(newStatus);

        Order updatedOrder =
                adminOrderHelper.save(order);

        return orderResponseBuilder.build(updatedOrder);
    }

    @Override
    public OrderResponse confirmOrder(Long orderId) {

        return updateOrderStatus(
                orderId,
                OrderStatus.CONFIRMED);
    }

    @Override
    public OrderResponse packOrder(Long orderId) {

        return updateOrderStatus(
                orderId,
                OrderStatus.PACKED);
    }

    @Override
    public OrderResponse outForDelivery(Long orderId) {

        return updateOrderStatus(
                orderId,
                OrderStatus.OUT_FOR_DELIVERY);
    }

    @Override
    public OrderResponse deliverOrder(Long orderId) {

        return updateOrderStatus(
                orderId,
                OrderStatus.DELIVERED);
    }

    @Override
    public OrderResponse cancelOrder(Long orderId) {

        return updateOrderStatus(
                orderId,
                OrderStatus.CANCELLED);
    }
}