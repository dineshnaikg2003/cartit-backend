package com.cartit.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.cartit.enums.DiscountType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public class UpdateCouponRequest {

    @Size(max = 50, message = "Coupon code must not exceed 50 characters")
    private String code;

    private DiscountType discountType;

    @DecimalMin(value = "0.01", message = "Discount value must be greater than 0")
    private BigDecimal discountValue;

    @PositiveOrZero(message = "Minimum order amount cannot be negative")
    private BigDecimal minimumOrderAmount;

    @PositiveOrZero(message = "Maximum discount cannot be negative")
    private BigDecimal maximumDiscount;

    private LocalDateTime startDate;

    private LocalDateTime expiryDate;

    @PositiveOrZero(message = "Usage limit cannot be negative")
    private Integer usageLimit;

    private Boolean active;

    private Boolean oneUsagePerUser;

    public UpdateCouponRequest() {
    }

	public String getCode() {
		return code;
	}

	public Boolean getOneUsagePerUser() {
		return oneUsagePerUser;
	}

	public void setOneUsagePerUser(Boolean oneUsagePerUser) {
		this.oneUsagePerUser = oneUsagePerUser;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public DiscountType getDiscountType() {
		return discountType;
	}

	public void setDiscountType(DiscountType discountType) {
		this.discountType = discountType;
	}

	public BigDecimal getDiscountValue() {
		return discountValue;
	}

	public void setDiscountValue(BigDecimal discountValue) {
		this.discountValue = discountValue;
	}

	public BigDecimal getMinimumOrderAmount() {
		return minimumOrderAmount;
	}

	public void setMinimumOrderAmount(BigDecimal minimumOrderAmount) {
		this.minimumOrderAmount = minimumOrderAmount;
	}

	public BigDecimal getMaximumDiscount() {
		return maximumDiscount;
	}

	public void setMaximumDiscount(BigDecimal maximumDiscount) {
		this.maximumDiscount = maximumDiscount;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public LocalDateTime getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(LocalDateTime expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Integer getUsageLimit() {
		return usageLimit;
	}

	public void setUsageLimit(Integer usageLimit) {
		this.usageLimit = usageLimit;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

}
