package com.cartit.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cartit.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByCartIdAndActiveTrue(Long cartId);

    Optional<CartItem> findByCartIdAndProductIdAndActiveTrue(
            Long cartId,
            Long productId);

    boolean existsByCartIdAndProductIdAndActiveTrue(
            Long cartId,
            Long productId);

	void deleteByCartId(Long cartId);
}