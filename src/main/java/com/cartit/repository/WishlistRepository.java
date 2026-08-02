package com.cartit.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cartit.entity.Wishlist;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    boolean existsByUserIdAndProductId(Long userId, Long productId);

    List<Wishlist> findByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<Wishlist> findByUserIdAndProductId(Long userId,
                                                Long productId);
}