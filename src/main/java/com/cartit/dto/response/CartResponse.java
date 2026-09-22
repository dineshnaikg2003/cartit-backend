package com.cartit.dto.response;

import java.math.BigDecimal;
import java.util.List;

public class CartResponse {

    private Long cartId;

    private Integer totalItems;

    private BigDecimal subTotal;

    private List<CartItemResponse> items;

    public CartResponse() {
    }

    public CartResponse(
            Long cartId,
            Integer totalItems,
            BigDecimal subTotal,
            List<CartItemResponse> items) {

        this.cartId = cartId;
        this.totalItems = totalItems;
        this.subTotal = subTotal;
        this.items = items;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    private Long claimedOfferId;

    public Long getClaimedOfferId() {
        return claimedOfferId;
    }

    public void setClaimedOfferId(Long claimedOfferId) {
        this.claimedOfferId = claimedOfferId;
    }

    public List<CartItemResponse> getItems() {
        return items;
    }

    public void setItems(List<CartItemResponse> items) {
        this.items = items;
    }
}