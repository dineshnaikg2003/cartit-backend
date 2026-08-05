package com.cartit.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.cartit.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>,JpaSpecificationExecutor<Product> {

    Optional<Product> findBySku(String sku);

    boolean existsBySku(String sku);

    List<Product> findByActiveTrueOrderByNameAsc();

    List<Product> findByCategoryIdAndActiveTrue(Long categoryId);

    List<Product> findByBrandIdAndActiveTrue(Long brandId);

    List<Product> findByFeaturedTrueAndActiveTrue();
    
    Optional<Product> findByIdAndActiveTrue(Long id);
    
    long countByActiveTrue();

    long countByStockLessThanAndActiveTrue(Integer stock);
}