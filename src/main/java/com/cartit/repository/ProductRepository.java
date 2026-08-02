package com.cartit.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cartit.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findBySku(String sku);

    boolean existsBySku(String sku);

    List<Product> findByActiveTrueOrderByNameAsc();

    List<Product> findByCategoryIdAndActiveTrue(Long categoryId);

    List<Product> findByBrandIdAndActiveTrue(Long brandId);

    List<Product> findByFeaturedTrueAndActiveTrue();
}