package com.cartit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cartit.entity.OrderItem;

public interface OrderItemRepository
        extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrderIdAndActiveTrue(Long orderId);
}