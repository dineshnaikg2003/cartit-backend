package com.cartit.dto.response;

import java.math.BigDecimal;

public class CouponValidationResponse {

    private boolean valid;
    private String code;
    private String message;
    private BigDecimal discount;
    private BigDecimal subtotal;
    private BigDecimal finalAmount;

    public CouponValidationResponse() {
    }

    public CouponValidationResponse(
            boolean valid,
            String code,
            String message,
            BigDecimal discount,
            BigDecimal subtotal,
            BigDecimal finalAmount) {

        this.valid = valid;
        this.code = code;
        this.message = message;
        this.discount = discount;
        this.subtotal = subtotal;
        this.finalAmount = finalAmount;
    }

    public boolean isValid() {
        return valid;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getFinalAmount() {
        return finalAmount;
    }
}