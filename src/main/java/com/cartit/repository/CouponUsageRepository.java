package com.cartit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cartit.entity.CouponUsage;

public interface CouponUsageRepository
        extends JpaRepository<CouponUsage, Long> {

    boolean existsByCouponIdAndUserId(
            Long couponId,
            Long userId);
}