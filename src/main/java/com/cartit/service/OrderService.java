package com.cartit.service;

import java.util.List;

import com.cartit.dto.response.OrderResponse;

public interface OrderService {

    List<OrderResponse> getMyOrders();

    OrderResponse getOrder(Long orderId);

    OrderResponse getOrderByOrderNumber(
            String orderNumber);

    OrderResponse cancelOrder(Long orderId);

}
