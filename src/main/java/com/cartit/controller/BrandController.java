package com.cartit.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cartit.dto.request.BrandRequest;
import com.cartit.dto.response.ApiResponse;
import com.cartit.dto.response.BrandResponse;
import com.cartit.service.BrandService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/brands")
public class BrandController {

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BrandResponse>> createBrand(
            @Valid @RequestBody BrandRequest request) {

        BrandResponse response = brandService.createBrand(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        true,
                        "Brand created successfully",
                        response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BrandResponse>>> getAllBrands() {

        List<BrandResponse> response = brandService.getAllBrands();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Brands fetched successfully",
                        response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BrandResponse>> getBrandById(
            @PathVariable Long id) {

        BrandResponse response = brandService.getBrandById(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Brand fetched successfully",
                        response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BrandResponse>> updateBrand(
            @PathVariable Long id,
            @Valid @RequestBody BrandRequest request) {

        BrandResponse response =
                brandService.updateBrand(id, request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Brand updated successfully",
                        response));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<BrandResponse>> activateBrand(
            @PathVariable Long id) {

        BrandResponse response =
                brandService.activateBrand(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Brand activated successfully",
                        response));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<BrandResponse>> deactivateBrand(
            @PathVariable Long id) {

        BrandResponse response =
                brandService.deactivateBrand(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Brand deactivated successfully",
                        response));
    }
}