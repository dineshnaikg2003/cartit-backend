package com.cartit.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cartit.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderNumber(String orderNumber);

    Optional<Order> findByIdAndUserIdAndActiveTrue(
            Long orderId,
            Long userId);

    List<Order> findByUserIdAndActiveTrueOrderByCreatedAtDesc(
            Long userId);
}