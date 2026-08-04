package com.cartit.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cartit.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByIdAndUserIdAndActiveTrue(Long id, Long userId);

    Optional<Order> findByOrderNumber(String orderNumber);

    List<Order> findByUserIdAndActiveTrueOrderByCreatedAtDesc(Long userId);

    // ===== Admin =====

    Optional<Order> findByIdAndActiveTrue(Long id);

    List<Order> findByActiveTrueOrderByCreatedAtDesc();

}