package com.cartit.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cartit.dto.request.ProductRequest;
import com.cartit.dto.response.ApiResponse;
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

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody ProductRequest request) {

        ProductResponse response = productService.createProduct(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        true,
                        "Product created successfully",
                        response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts() {

        List<ProductResponse> response = productService.getAllProducts();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Products fetched successfully",
                        response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(
            @PathVariable Long id) {

        ProductResponse response = productService.getProductById(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Product fetched successfully",
                        response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {

        ProductResponse response =
                productService.updateProduct(id, request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Product updated successfully",
                        response));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<ProductResponse>> activateProduct(
            @PathVariable Long id) {

        ProductResponse response =
                productService.activateProduct(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Product activated successfully",
                        response));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<ProductResponse>> deactivateProduct(
            @PathVariable Long id) {

        ProductResponse response =
                productService.deactivateProduct(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Product deactivated successfully",
                        response));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProductsByCategory(
            @PathVariable Long categoryId) {

        List<ProductResponse> response =
                productService.getProductsByCategory(categoryId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Products fetched successfully",
                        response));
    }

    @GetMapping("/brand/{brandId}")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProductsByBrand(
            @PathVariable Long brandId) {

        List<ProductResponse> response =
                productService.getProductsByBrand(brandId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Products fetched successfully",
                        response));
    }

    @GetMapping("/featured")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getFeaturedProducts() {

        List<ProductResponse> response =
                productService.getFeaturedProducts();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Featured products fetched successfully",
                        response));
    }

}