package com.cartit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cartit.entity.OrderItem;

public interface OrderItemRepository
extends JpaRepository<OrderItem, Long> {
}