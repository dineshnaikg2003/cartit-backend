package com.cartit.service.builder;

import java.util.List;

import org.springframework.stereotype.Component;

import com.cartit.dto.response.OrderItemResponse;
import com.cartit.dto.response.OrderResponse;
import com.cartit.entity.Order;
import com.cartit.entity.OrderItem;
import com.cartit.mapper.OrderMapper;

@Component
public class OrderResponseBuilder {

    public OrderResponse build(Order order) {

        List<OrderItemResponse> items = order.getOrderItems()
                .stream()
                .filter(OrderItem::getActive)
                .map(OrderMapper::toOrderItemResponse)
                .toList();

        OrderResponse response =
                OrderMapper.toOrderResponse(order, items);

        response.setTotalItems(items.size());

        return response;
    }

    public List<OrderResponse> build(List<Order> orders) {

        return orders.stream()
                .map(this::build)
                .toList();
    }
}