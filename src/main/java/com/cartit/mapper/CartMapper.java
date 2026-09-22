package com.cartit.mapper;

import java.util.List;

import com.cartit.dto.response.CartItemResponse;
import com.cartit.dto.response.CartResponse;
import com.cartit.dto.response.common.ProductSummaryResponse;
import com.cartit.entity.Cart;
import com.cartit.entity.CartItem;

public final class CartMapper {

    private CartMapper() {
    }

    public static CartItemResponse toCartItemResponse(
            CartItem cartItem,
            ProductSummaryResponse product) {

        return new CartItemResponse(
                cartItem.getId(),
                cartItem.getQuantity(),
                cartItem.getUnitPrice(),
                cartItem.getTotalPrice(),
                product
        );
    }

    public static CartResponse toCartResponse(
            Cart cart,
            List<CartItemResponse> items) {

        CartResponse response = new CartResponse(
                cart.getId(),
                cart.getTotalItems(),
                cart.getSubTotal(),
                items
        );
        response.setClaimedOfferId(cart.getClaimedOffer() != null ? cart.getClaimedOffer().getId() : null);
        return response;
    }
}