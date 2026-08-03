package com.cartit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cartit.entity.Order;

public interface OrderRepository
        extends JpaRepository <Order, Long> {
}