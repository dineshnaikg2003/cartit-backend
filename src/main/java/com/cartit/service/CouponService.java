package com.cartit.service;

import java.util.List;

import com.cartit.dto.request.CreateCouponRequest;
import com.cartit.dto.request.UpdateCouponRequest;
import com.cartit.dto.request.ValidateCouponRequest;
import com.cartit.dto.response.CouponResponse;
import com.cartit.dto.response.CouponValidationResponse;

public interface CouponService {

    CouponResponse createCoupon(CreateCouponRequest request);

    CouponResponse getCoupon(Long id);

    List<CouponResponse> getAllCoupons();

    CouponResponse updateCoupon(
            Long id,
            UpdateCouponRequest request);

    void deleteCoupon(Long id);

    CouponResponse toggleCoupon(Long id);

    CouponValidationResponse validateCoupon(
            ValidateCouponRequest request);
}
