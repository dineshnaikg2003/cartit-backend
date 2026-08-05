package com.cartit.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cartit.dto.request.ProductSearchRequest;
import com.cartit.dto.request.UpdateStockRequest;
import com.cartit.dto.response.ApiResponse;
import com.cartit.dto.response.PageResponse;
import com.cartit.dto.response.ProductResponse;
import com.cartit.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> getAllProducts(
            @ModelAttribute ProductSearchRequest request) {

        PageResponse<ProductResponse> response =
                productService.getAllProducts(request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Products fetched successfully.",
                        response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(
            @PathVariable Long id) {

        ProductResponse response =
                productService.getProductById(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Product fetched successfully.",
                        response));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProductsByCategory(
            @PathVariable Long categoryId) {

        List<ProductResponse> response =
                productService.getProductsByCategory(categoryId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Products fetched successfully.",
                        response));
    }

    @GetMapping("/brand/{brandId}")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProductsByBrand(
            @PathVariable Long brandId) {

        List<ProductResponse> response =
                productService.getProductsByBrand(brandId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Products fetched successfully.",
                        response));
    }

    @GetMapping("/featured")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getFeaturedProducts() {

        List<ProductResponse> response =
                productService.getFeaturedProducts();

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Featured products fetched successfully.",
                        response));
    }
    
    @PatchMapping("/{id}/stock")
    public ResponseEntity<ApiResponse<ProductResponse>> updateStock(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStockRequest request) {

        ProductResponse response =
                productService.updateStock(
                        id,
                        request.getStock());

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Product stock updated successfully.",
                        response));
    }
}