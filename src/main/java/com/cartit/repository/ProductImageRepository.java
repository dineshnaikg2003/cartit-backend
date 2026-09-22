package com.cartit.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cartit.entity.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    List<ProductImage> findByProductIdAndActiveTrueOrderByDisplayOrderAsc(
            Long productId);

    List<ProductImage> findByProductId(
            Long productId);

    Optional<ProductImage> findByProductIdAndPrimaryImageTrue(
            Long productId);

    Optional<ProductImage> findByProductIdAndPrimaryImageTrueAndActiveTrue(
            Long productId);

    boolean existsByProductIdAndDisplayOrderAndActiveTrue(
            Long productId,
            Integer displayOrder);

    Optional<ProductImage> findByProductIdAndDisplayOrderAndActiveTrue(
            Long productId,
            Integer displayOrder);

    long countByProductIdAndActiveTrue(
            Long productId);

    List<ProductImage> findByProductIdAndDisplayOrderGreaterThanEqualAndActiveTrueOrderByDisplayOrderAsc(
            Long productId,
            Integer displayOrder);

    // Needed when updating an existing image
    List<ProductImage> findByProductIdAndDisplayOrderGreaterThanEqualAndActiveTrueAndIdNotOrderByDisplayOrderAsc(
            Long productId,
            Integer displayOrder,
            Long id);

    void deleteByProductId(Long productId);
}
