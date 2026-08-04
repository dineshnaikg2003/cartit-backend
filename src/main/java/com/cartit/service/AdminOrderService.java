package com.cartit.service;

import java.util.List;

import com.cartit.dto.response.OrderResponse;

public interface AdminOrderService {

    List<OrderResponse> getAllOrders();

    OrderResponse getOrder(Long orderId);

    OrderResponse confirmOrder(Long orderId);

    OrderResponse packOrder(Long orderId);

    OrderResponse outForDelivery(Long orderId);

    OrderResponse deliverOrder(Long orderId);

    OrderResponse cancelOrder(Long orderId);
}