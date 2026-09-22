package com.cartit.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;

public class UpdateOfferRequest {

    private String name;

    private String description;

    @DecimalMin(
            value = "0.01",
            message = "Minimum purchase amount must be greater than 0"
    )
    private BigDecimal minimumPurchaseAmount;

    private Long rewardProductId;

    @DecimalMin(
            value = "0.00",
            message = "Reward price cannot be negative"
    )
    private BigDecimal rewardPrice;

    public BigDecimal getRewardPrice() {
        return rewardPrice;
    }

    public void setRewardPrice(BigDecimal rewardPrice) {
        this.rewardPrice = rewardPrice;
    }

    @Min(
            value = 1,
            message = "Reward quantity must be at least 1"
    )
    private Integer rewardQuantity;

    private LocalDateTime startDate;

    private LocalDateTime expiryDate;

    private Boolean active;

    private com.cartit.enums.Unit unit;

    public com.cartit.enums.Unit getUnit() {
        return unit;
    }

    public void setUnit(com.cartit.enums.Unit unit) {
        this.unit = unit;
    }

    public UpdateOfferRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getMinimumPurchaseAmount() {
        return minimumPurchaseAmount;
    }

    public void setMinimumPurchaseAmount(
            BigDecimal minimumPurchaseAmount) {
        this.minimumPurchaseAmount = minimumPurchaseAmount;
    }

    public Long getRewardProductId() {
        return rewardProductId;
    }

    public void setRewardProductId(Long rewardProductId) {
        this.rewardProductId = rewardProductId;
    }

    public Integer getRewardQuantity() {
        return rewardQuantity;
    }

    public void setRewardQuantity(
            Integer rewardQuantity) {
        this.rewardQuantity = rewardQuantity;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(
            LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(
            LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}