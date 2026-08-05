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

}