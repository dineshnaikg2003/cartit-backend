package com.cartit.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cartit.dto.request.ValidateCouponRequest;
import com.cartit.dto.response.ApiResponse;
import com.cartit.dto.response.CouponValidationResponse;
import com.cartit.service.CouponService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/coupons")
public class CouponValidationController {

    private final CouponService couponService;

    public CouponValidationController(
            CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping("/validate")
    public ResponseEntity<ApiResponse<CouponValidationResponse>> validateCoupon(
            @Valid @RequestBody ValidateCouponRequest request) {

        CouponValidationResponse response =
                couponService.validateCoupon(request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Coupon validated successfully",
                        response));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<java.util.List<com.cartit.dto.response.CouponResponse>>> getActiveCoupons() {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Active coupons fetched successfully",
                        couponService.getAllCoupons().stream().filter(c -> c.isActive()).toList()));
    }
}