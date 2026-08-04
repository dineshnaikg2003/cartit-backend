package com.cartit.service.helper;

import java.util.List;

import com.cartit.entity.Order;
import com.cartit.entity.OrderItem;

public interface OrderHelper {

    Order getOrder(Long orderId);

    Order getOrder(String orderNumber);

    List<Order> getMyOrders();

    Order save(Order order);

    OrderItem save(OrderItem orderItem);

    List<OrderItem> saveAllOrderItems(List<OrderItem> orderItems);
}