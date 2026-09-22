package com.cartit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cartit.entity.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    Optional<Coupon> findByCodeIgnoreCase(String code);

    boolean existsByCodeIgnoreCase(String code);
}