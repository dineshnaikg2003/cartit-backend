package com.cartit.service;

import java.util.List;

import com.cartit.dto.response.OrderResponse;

public interface OrderService {

    List<OrderResponse> getMyOrders();

    OrderResponse getOrder(Long orderId);

    OrderResponse getOrderByOrderNumber(
            String orderNumber);

    OrderResponse cancelOrder(Long orderId);

    OrderResponse rateOrder(Long orderId, Integer rating, String reviewComment);

    com.cartit.dto.response.RouteResponse getOrderRoute(Long orderId, Double originLat, Double originLng);
}
