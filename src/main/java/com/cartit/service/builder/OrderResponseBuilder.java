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

    private final com.cartit.service.StoreService storeService;

    public OrderResponseBuilder(com.cartit.service.StoreService storeService) {
        this.storeService = storeService;
    }

    public OrderResponse build(Order order) {

        List<OrderItemResponse> items = order.getOrderItems()
                .stream()
                .filter(OrderItem::getActive)
                .map(OrderMapper::toOrderItemResponse)
                .toList();

        OrderResponse response =
                OrderMapper.toOrderResponse(order, items);

        response.setTotalItems(items.size());

        com.cartit.entity.Store store = storeService.getStore();
        if (store != null) {
            response.setStoreName(store.getName());
            response.setStoreAddress(store.getAddressLine1() + (store.getCity() != null ? ", " + store.getCity() : ""));
            response.setStoreLatitude(store.getLatitude());
            response.setStoreLongitude(store.getLongitude());
        }

        if (order.getDeliveryLatitude() != null && order.getDeliveryLongitude() != null && store != null) {
            double distance = storeService.calculateDistanceKm(
                store.getLatitude(), store.getLongitude(),
                order.getDeliveryLatitude(), order.getDeliveryLongitude()
            );
            response.setDistanceKm(Math.round(distance * 10.0) / 10.0);
        }

        return response;
    }

    public List<OrderResponse> build(List<Order> orders) {

        return orders.stream()
                .map(this::build)
                .toList();
    }
}