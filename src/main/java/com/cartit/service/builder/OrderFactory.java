package com.cartit.service.builder;

import java.util.List;

import com.cartit.dto.request.CheckoutRequest;
import com.cartit.entity.Address;
import com.cartit.entity.Cart;
import com.cartit.entity.Coupon;
import com.cartit.entity.Order;
import com.cartit.entity.OrderItem;

public interface OrderFactory {

	Order createOrder(
	        Cart cart,
	        Address address,
	        CheckoutRequest request,
	        Coupon coupon);

    List<OrderItem> createOrderItems(
            Order order,
            Cart cart);
}