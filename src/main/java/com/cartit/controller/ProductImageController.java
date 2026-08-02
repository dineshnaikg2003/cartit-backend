package com.cartit.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cartit.dto.request.ProductImageRequest;
import com.cartit.dto.response.ApiResponse;
import com.cartit.dto.response.ProductImageResponse;
import com.cartit.service.ProductImageService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/product-images")
public class ProductImageController {

    private final ProductImageService productImageService;

    public ProductImageController(ProductImageService productImageService) {
        this.productImageService = productImageService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductImageResponse>> addImage(
            @Valid @RequestBody ProductImageRequest request) {

        ProductImageResponse response = productImageService.addImage(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        true,
                        "Image added successfully",
                        response));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<List<ProductImageResponse>>> getProductImages(
            @PathVariable Long productId) {

        List<ProductImageResponse> response =
                productImageService.getProductImages(productId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Images fetched successfully",
                        response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductImageResponse>> updateImage(
            @PathVariable Long id,
            @Valid @RequestBody ProductImageRequest request) {

        ProductImageResponse response =
                productImageService.updateImage(id, request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Image updated successfully",
                        response));
    }

    @PatchMapping("/{id}/primary")
    public ResponseEntity<ApiResponse<ProductImageResponse>> setPrimaryImage(
            @PathVariable Long id) {

        ProductImageResponse response =
                productImageService.setPrimaryImage(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Primary image updated successfully",
                        response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteImage(
            @PathVariable Long id) {

        productImageService.deleteImage(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Image deleted successfully",
                        null));
    }

}