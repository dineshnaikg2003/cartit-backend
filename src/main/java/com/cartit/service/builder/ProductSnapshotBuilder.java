package com.cartit.service.builder;

import com.cartit.entity.CartItem;
import com.cartit.entity.Order;
import com.cartit.entity.OrderItem;

public interface ProductSnapshotBuilder {

    OrderItem build(
            Order order,
            CartItem cartItem);

}