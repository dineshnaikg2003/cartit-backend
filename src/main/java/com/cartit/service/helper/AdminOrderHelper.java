package com.cartit.service.helper;

import java.util.List;

import com.cartit.entity.Order;

public interface AdminOrderHelper {

    List<Order> getAllOrders();

    Order getOrder(Long orderId);

    Order save(Order order);

}