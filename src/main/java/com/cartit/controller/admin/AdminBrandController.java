package com.cartit.controller.admin;

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
@RequestMapping("/api/admin/brands")
public class AdminBrandController {

    private final BrandService brandService;

    public AdminBrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BrandResponse>> createBrand(
            @Valid @RequestBody BrandRequest request) {

        BrandResponse response =
                brandService.createBrand(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Brand created successfully.",
                        response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BrandResponse>>> getAllBrands() {

        List<BrandResponse> response =
                brandService.getAllBrands();

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Brands fetched successfully.",
                        response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BrandResponse>> getBrandById(
            @PathVariable Long id) {

        BrandResponse response =
                brandService.getBrandById(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Brand fetched successfully.",
                        response));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<BrandResponse>>> getAllBrandsForAdmin() {

        List<BrandResponse> response =
                brandService.getAllBrandsForAdmin();

        return ResponseEntity.ok(
                ApiResponse.success(
                        "All brands fetched successfully.",
                        response
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BrandResponse>> updateBrand(
            @PathVariable Long id,
            @Valid @RequestBody BrandRequest request) {

        BrandResponse response =
                brandService.updateBrand(id, request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Brand updated successfully.",
                        response));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<BrandResponse>> activateBrand(
            @PathVariable Long id) {

        BrandResponse response =
                brandService.activateBrand(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Brand activated successfully.",
                        response));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<BrandResponse>> deactivateBrand(
            @PathVariable Long id) {

        BrandResponse response =
                brandService.deactivateBrand(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Brand deactivated successfully.",
                        response));
    }
}
