package com.cartit.service;

import com.cartit.entity.Order;
import com.cartit.entity.Cart;

public interface InventoryService {

    void reduceStock(Cart cart);

    void restoreStock(Order order);

}