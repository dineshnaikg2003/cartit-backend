package com.cartit.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cartit.dto.request.CreateCouponRequest;
import com.cartit.dto.request.UpdateCouponRequest;
import com.cartit.dto.response.ApiResponse;
import com.cartit.dto.response.CouponResponse;
import com.cartit.service.CouponService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/coupons")
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CouponResponse>> createCoupon(
            @Valid @RequestBody CreateCouponRequest request) {

        CouponResponse coupon =
                couponService.createCoupon(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        new ApiResponse<>(
                                true,
                                "Coupon created successfully",
                                coupon));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CouponResponse>>> getAllCoupons() {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Coupons fetched successfully",
                        couponService.getAllCoupons()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CouponResponse>> getCoupon(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Coupon fetched successfully",
                        couponService.getCoupon(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CouponResponse>> updateCoupon(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCouponRequest request) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Coupon updated successfully",
                        couponService.updateCoupon(id, request)));
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<ApiResponse<CouponResponse>> toggleCoupon(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Coupon status updated successfully",
                        couponService.toggleCoupon(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCoupon(
            @PathVariable Long id) {

        couponService.deleteCoupon(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Coupon deleted successfully",
                        null));
    }
}