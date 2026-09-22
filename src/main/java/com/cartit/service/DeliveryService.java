package com.cartit.service;

import java.util.List;
import com.cartit.dto.response.OrderResponse;
import com.cartit.enums.OrderStatus;

public interface DeliveryService {

    List<OrderResponse> getAllocatedOrders();

    void updateLocation(Double latitude, Double longitude);

    OrderResponse updateOrderStatus(Long orderId, OrderStatus status);

    OrderResponse acceptOrder(Long orderId);
}
