package com.cartit.mapper;

import com.cartit.dto.response.WishlistResponse;
import com.cartit.dto.response.common.ProductSummaryResponse;
import com.cartit.entity.Wishlist;

public final class WishlistMapper {

    private WishlistMapper() {
    }

    public static WishlistResponse toResponse(
            Wishlist wishlist,
            ProductSummaryResponse product) {

        return new WishlistResponse(
                wishlist.getId(),
                product
        );
    }

}