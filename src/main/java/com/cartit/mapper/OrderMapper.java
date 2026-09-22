package com.cartit.mapper;

import java.util.List;

import com.cartit.dto.response.OrderItemResponse;
import com.cartit.dto.response.OrderResponse;
import com.cartit.entity.Order;
import com.cartit.entity.OrderItem;

public final class OrderMapper {

    private OrderMapper() {
    }

    public static OrderItemResponse toOrderItemResponse(
            OrderItem item) {

        return new OrderItemResponse(
                item.getId(),
                item.getProductId(),
                item.getProductSku(),
                item.getProductName(),
                item.getProductImageUrl(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getTotalPrice());
    }

    public static OrderResponse toOrderResponse(
            Order order,
            List<OrderItemResponse> items) {

        OrderResponse response = new OrderResponse();

        response.setId(order.getId());
        response.setOrderNumber(order.getOrderNumber());

        response.setDeliveryName(order.getDeliveryName());
        response.setDeliveryPhone(order.getDeliveryPhone());
        response.setDeliveryAlternatePhone(order.getDeliveryAlternatePhone());
        response.setDeliveryAddressLine1(order.getDeliveryAddressLine1());
        response.setDeliveryAddressLine2(order.getDeliveryAddressLine2());
        response.setDeliveryLandmark(order.getDeliveryLandmark());
        response.setDeliveryCity(order.getDeliveryCity());
        response.setDeliveryState(order.getDeliveryState());
        response.setDeliveryCountry(order.getDeliveryCountry());
        response.setDeliveryPostalCode(order.getDeliveryPostalCode());
        response.setDeliveryAddressType(order.getDeliveryAddressType());
        response.setDeliveryLatitude(order.getDeliveryLatitude());
        response.setDeliveryLongitude(order.getDeliveryLongitude());

        response.setOrderStatus(order.getOrderStatus());
        response.setPaymentStatus(order.getPaymentStatus());
        response.setPaymentMethod(order.getPaymentMethod());

        response.setSubTotal(order.getSubTotal());
        response.setDeliveryCharge(order.getDeliveryCharge());
        response.setDiscount(order.getDiscount());
        response.setTax(order.getTax());
        response.setTotalAmount(order.getTotalAmount());

        response.setPlacedAt(order.getPlacedAt());
        response.setRating(order.getRating());
        response.setReviewComment(order.getReviewComment());
        response.setRatedAt(order.getRatedAt());

        if (order.getDeliveryBoy() != null) {
            response.setDeliveryBoyId(order.getDeliveryBoy().getId());
            response.setDeliveryBoyName(order.getDeliveryBoy().getName());
            response.setDeliveryBoyPhone(order.getDeliveryBoy().getPhone());
        }

        response.setCurrentDeliveryLatitude(order.getCurrentDeliveryLatitude());
        response.setCurrentDeliveryLongitude(order.getCurrentDeliveryLongitude());

        response.setItems(items);

        return response;
    }
}