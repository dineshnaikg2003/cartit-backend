package com.cartit.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OfferResponse {

    private Long id;
    private String name;
    private String description;
    private BigDecimal minimumPurchaseAmount;

    private Long rewardProductId;
    private String rewardProductName;
    private String rewardProductImageUrl;

    private Integer rewardQuantity;

    private LocalDateTime startDate;
    private LocalDateTime expiryDate;

    private boolean active;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BigDecimal rewardPrice;
    private com.cartit.enums.Unit unit;

    public com.cartit.enums.Unit getUnit() {
        return unit;
    }

    public void setUnit(com.cartit.enums.Unit unit) {
        this.unit = unit;
    }

    public BigDecimal getRewardPrice() {
		return rewardPrice;
	}

	public void setRewardPrice(BigDecimal rewardPrice) {
		this.rewardPrice = rewardPrice;
	}

	public OfferResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        this.minimumPurchaseAmount =
                minimumPurchaseAmount;
    }

    public Long getRewardProductId() {
        return rewardProductId;
    }

    public void setRewardProductId(Long rewardProductId) {
        this.rewardProductId =
                rewardProductId;
    }

    public String getRewardProductName() {
        return rewardProductName;
    }

    public void setRewardProductName(
            String rewardProductName) {
        this.rewardProductName =
                rewardProductName;
    }

    public String getRewardProductImageUrl() {
        return rewardProductImageUrl;
    }

    public void setRewardProductImageUrl(String rewardProductImageUrl) {
        this.rewardProductImageUrl = rewardProductImageUrl;
    }

    public Integer getRewardQuantity() {
        return rewardQuantity;
    }

    public void setRewardQuantity(
            Integer rewardQuantity) {
        this.rewardQuantity =
                rewardQuantity;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(
            LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(
            LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}