package com.cartit.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cartit.dto.request.CreateCouponRequest;
import com.cartit.dto.request.UpdateCouponRequest;
import com.cartit.dto.request.ValidateCouponRequest;
import com.cartit.dto.response.CouponResponse;
import com.cartit.dto.response.CouponValidationResponse;
import com.cartit.entity.Coupon;
import com.cartit.entity.User;
import com.cartit.enums.DiscountType;
import com.cartit.exception.BadRequestException;
import com.cartit.exception.ResourceNotFoundException;
import com.cartit.repository.CouponRepository;
import com.cartit.repository.CouponUsageRepository;
import com.cartit.security.CurrentUserService;
import com.cartit.service.CouponService;

@Service
@Transactional
public class CouponServiceImpl implements CouponService {

	private final CouponRepository couponRepository;
	private final CouponUsageRepository couponUsageRepository;
	private final CurrentUserService currentUserService;
	public CouponServiceImpl(
	        CouponRepository couponRepository,
	        CouponUsageRepository couponUsageRepository,
	        CurrentUserService currentUserService) {

	    this.couponRepository = couponRepository;
	    this.couponUsageRepository = couponUsageRepository;
	    this.currentUserService = currentUserService;
	}

	@Override
	@Transactional(readOnly = true)
	public CouponValidationResponse validateCoupon(ValidateCouponRequest request) {

		String code = normalizeCode(request.getCode());

		Coupon coupon = couponRepository.findByCodeIgnoreCase(code)
				.orElseThrow(() -> new BadRequestException("Invalid coupon code."));

		LocalDateTime now = LocalDateTime.now();

		if (!coupon.isActive()) {
			throw new BadRequestException("This coupon is inactive.");
		}

		if (now.isBefore(coupon.getStartDate())) {
			throw new BadRequestException("This coupon is not active yet.");
		}

		if (now.isAfter(coupon.getExpiryDate())) {
			throw new BadRequestException("This coupon has expired.");
		}

		if (coupon.getUsageLimit() != null && coupon.getUsedCount() >= coupon.getUsageLimit()) {

			throw new BadRequestException("This coupon has reached its usage limit.");
		}

		User currentUser = currentUserService.getCurrentUser();

		if (coupon.isOneUsagePerUser()
		        && couponUsageRepository.existsByCouponIdAndUserId(
		                coupon.getId(),
		                currentUser.getId())) {

		    throw new BadRequestException(
		            "You have already used this coupon.");
		}

		BigDecimal subtotal = request.getSubtotal();

		if (coupon.getMinimumOrderAmount() != null && subtotal.compareTo(coupon.getMinimumOrderAmount()) < 0) {

			throw new BadRequestException("Minimum order amount is â‚¹" + coupon.getMinimumOrderAmount());
		}

		/*
		 * We will add the one-usage-per-user check here after we connect the
		 * authenticated user.
		 */

		BigDecimal discount;

		if (coupon.getDiscountType() == DiscountType.PERCENTAGE) {

			discount = subtotal.multiply(coupon.getDiscountValue()).divide(BigDecimal.valueOf(100));

			if (coupon.getMaximumDiscount() != null && discount.compareTo(coupon.getMaximumDiscount()) > 0) {

				discount = coupon.getMaximumDiscount();
			}

		} else {

			discount = coupon.getDiscountValue();

			if (discount.compareTo(subtotal) > 0) {
				discount = subtotal;
			}
		}

		BigDecimal finalAmount = subtotal.subtract(discount);

		return new CouponValidationResponse(true, coupon.getCode(), "Coupon applied successfully.", discount, subtotal,
				finalAmount);
	}

	@Override
	public CouponResponse createCoupon(CreateCouponRequest request) {

		String code = normalizeCode(request.getCode());

		if (couponRepository.existsByCodeIgnoreCase(code)) {
			throw new BadRequestException("Coupon code is already registered.");
		}

		validateCoupon(request.getDiscountType(), request.getDiscountValue(), request.getStartDate(),
				request.getExpiryDate());

		Coupon coupon = new Coupon();

		coupon.setCode(code);
		coupon.setDiscountType(request.getDiscountType());
		coupon.setDiscountValue(request.getDiscountValue());
		coupon.setMinimumOrderAmount(request.getMinimumOrderAmount());
		coupon.setMaximumDiscount(request.getMaximumDiscount());
		coupon.setStartDate(request.getStartDate());
		coupon.setExpiryDate(request.getExpiryDate());
		coupon.setUsageLimit(request.getUsageLimit());
		coupon.setUsedCount(0);
		coupon.setActive(request.getActive() == null || request.getActive());
		coupon.setOneUsagePerUser(
		        request.getOneUsagePerUser() != null
		                && request.getOneUsagePerUser());

		return toResponse(couponRepository.save(coupon));
	}

	@Override
	@Transactional(readOnly = true)
	public CouponResponse getCoupon(Long id) {

		Coupon coupon = findCoupon(id);

		return toResponse(coupon);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CouponResponse> getAllCoupons() {

		return couponRepository.findAll().stream().map(this::toResponse).toList();
	}

	@Override
	public CouponResponse updateCoupon(Long id, UpdateCouponRequest request) {

		Coupon coupon = findCoupon(id);

		String code = request.getCode();

		if (code != null && !code.isBlank()) {

			code = normalizeCode(code);

			if (!code.equalsIgnoreCase(coupon.getCode()) && couponRepository.existsByCodeIgnoreCase(code)) {

				throw new BadRequestException("Coupon code is already registered.");
			}

			coupon.setCode(code);
		}

		DiscountType discountType = request.getDiscountType() != null ? request.getDiscountType()
				: coupon.getDiscountType();

		BigDecimal discountValue = request.getDiscountValue() != null ? request.getDiscountValue()
				: coupon.getDiscountValue();

		LocalDateTime startDate = request.getStartDate() != null ? request.getStartDate() : coupon.getStartDate();

		LocalDateTime expiryDate = request.getExpiryDate() != null ? request.getExpiryDate() : coupon.getExpiryDate();

		validateCoupon(discountType, discountValue, startDate, expiryDate);

		if (request.getDiscountType() != null) {
			coupon.setDiscountType(request.getDiscountType());
		}

		if (request.getDiscountValue() != null) {
			coupon.setDiscountValue(request.getDiscountValue());
		}

		if (request.getMinimumOrderAmount() != null) {
			coupon.setMinimumOrderAmount(request.getMinimumOrderAmount());
		}

		if (request.getMaximumDiscount() != null) {
			coupon.setMaximumDiscount(request.getMaximumDiscount());
		}

		if (request.getStartDate() != null) {
			coupon.setStartDate(request.getStartDate());
		}

		if (request.getExpiryDate() != null) {
			coupon.setExpiryDate(request.getExpiryDate());
		}

		if (request.getUsageLimit() != null) {

			if (request.getUsageLimit() < coupon.getUsedCount()) {

				throw new BadRequestException(
						"Usage limit cannot be less than " + "the number of times this coupon was used.");
			}

			coupon.setUsageLimit(request.getUsageLimit());
		}

		if (request.getActive() != null) {
			coupon.setActive(request.getActive());
		}

		if (request.getOneUsagePerUser() != null) {
		    coupon.setOneUsagePerUser(
		            request.getOneUsagePerUser());
		}

		return toResponse(couponRepository.save(coupon));
	}

	@Override
	public void deleteCoupon(Long id) {

		Coupon coupon = findCoupon(id);

		couponRepository.delete(coupon);
	}

	@Override
	public CouponResponse toggleCoupon(Long id) {

		Coupon coupon = findCoupon(id);

		coupon.setActive(!coupon.isActive());

		return toResponse(couponRepository.save(coupon));
	}

	private Coupon findCoupon(Long id) {

		return couponRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Coupon not found with id: " + id));
	}

	private String normalizeCode(String code) {

		return code.trim().toUpperCase();
	}

	private void validateCoupon(DiscountType discountType, BigDecimal discountValue, LocalDateTime startDate,
			LocalDateTime expiryDate) {

		if (discountType == null) {
			throw new BadRequestException("Discount type is required.");
		}

		if (discountValue == null || discountValue.compareTo(BigDecimal.ZERO) <= 0) {

			throw new BadRequestException("Discount value must be greater than 0.");
		}

		if (discountType == DiscountType.PERCENTAGE && discountValue.compareTo(BigDecimal.valueOf(100)) > 0) {

			throw new BadRequestException("Percentage discount cannot exceed 100%.");
		}

		if (startDate == null || expiryDate == null) {
			throw new BadRequestException("Start date and expiry date are required.");
		}

		if (!expiryDate.isAfter(startDate)) {
			throw new BadRequestException("Expiry date must be after start date.");
		}
	}

	private CouponResponse toResponse(Coupon coupon) {

		CouponResponse response = new CouponResponse();

		response.setId(coupon.getId());
		response.setCode(coupon.getCode());
		response.setDiscountType(coupon.getDiscountType());
		response.setDiscountValue(coupon.getDiscountValue());
		response.setMinimumOrderAmount(coupon.getMinimumOrderAmount());
		response.setMaximumDiscount(coupon.getMaximumDiscount());
		response.setStartDate(coupon.getStartDate());
		response.setExpiryDate(coupon.getExpiryDate());
		response.setUsageLimit(coupon.getUsageLimit());
		response.setUsedCount(coupon.getUsedCount());
		response.setActive(coupon.isActive());
		response.setCreatedAt(coupon.getCreatedAt());
		response.setUpdatedAt(coupon.getUpdatedAt());
		response.setOneUsagePerUser(
		        coupon.isOneUsagePerUser());

		return response;
	}
}
