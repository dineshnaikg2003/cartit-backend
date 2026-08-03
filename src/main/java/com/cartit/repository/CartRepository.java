package com.cartit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cartit.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUserIdAndActiveTrue(Long userId);
    
    Optional<Cart> findByIdAndActiveTrue(Long id);

    boolean existsByUserIdAndActiveTrue(Long userId);

}