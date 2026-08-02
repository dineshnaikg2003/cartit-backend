package com.cartit.dto.response;

import com.cartit.dto.response.common.ProductSummaryResponse;

public class WishlistResponse {

    private Long wishlistId;

    private ProductSummaryResponse product;

    public WishlistResponse() {
    }

    public WishlistResponse(Long wishlistId, ProductSummaryResponse product) {
        this.wishlistId = wishlistId;
        this.product = product;
    }

    public Long getWishlistId() {
        return wishlistId;
    }

    public void setWishlistId(Long wishlistId) {
        this.wishlistId = wishlistId;
    }

    public ProductSummaryResponse getProduct() {
        return product;
    }

    public void setProduct(ProductSummaryResponse product) {
        this.product = product;
    }
}