package com.cartit.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cartit.entity.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

	List<ProductImage> findByProductIdAndActiveTrueOrderByDisplayOrderAsc(Long productId);

	List<ProductImage> findByProductId(Long productId);

	Optional<ProductImage> findByProductIdAndPrimaryImageTrue(Long productId);

	Optional<ProductImage> findByProductIdAndPrimaryImageTrueAndActiveTrue(Long productId);
}