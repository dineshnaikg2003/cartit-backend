package com.cartit.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cartit.dto.response.ApiResponse;
import com.cartit.dto.response.ProductImageResponse;
import com.cartit.service.ProductImageService;

@RestController
@RequestMapping("/api/product-images")
public class ProductImageController {

    private final ProductImageService productImageService;

    public ProductImageController(ProductImageService productImageService) {
        this.productImageService = productImageService;
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<List<ProductImageResponse>>> getProductImages(
            @PathVariable Long productId) {

        List<ProductImageResponse> response =
                productImageService.getProductImages(productId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Product images fetched successfully.",
                        response));
    }
}