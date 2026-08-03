package com.cartit.dto.response;

import java.math.BigDecimal;

import com.cartit.dto.response.common.ProductSummaryResponse;

public class CartItemResponse {

    private Long cartItemId;

    private Integer quantity;

    private BigDecimal unitPrice;

    private BigDecimal totalPrice;

    private ProductSummaryResponse product;

    public CartItemResponse() {
    }

    public CartItemResponse(
            Long cartItemId,
            Integer quantity,
            BigDecimal unitPrice,
            BigDecimal totalPrice,
            ProductSummaryResponse product) {

        this.cartItemId = cartItemId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.product = product;
    }

    public Long getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(Long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ProductSummaryResponse getProduct() {
        return product;
    }

    public void setProduct(ProductSummaryResponse product) {
        this.product = product;
    }
}