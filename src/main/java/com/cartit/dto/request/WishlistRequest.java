package com.cartit.dto.request;

import jakarta.validation.constraints.NotNull;

public class WishlistRequest {

    @NotNull(message = "Product is required")
    private Long productId;

    public WishlistRequest() {
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}