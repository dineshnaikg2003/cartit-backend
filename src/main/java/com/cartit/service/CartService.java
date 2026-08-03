package com.cartit.service;

import com.cartit.dto.request.CartRequest;
import com.cartit.dto.response.CartResponse;

public interface CartService {

    CartResponse addToCart(CartRequest request);

    CartResponse getMyCart();

    CartResponse increaseQuantity(Long productId);

    CartResponse decreaseQuantity(Long productId);

    CartResponse removeFromCart(Long productId);

    CartResponse clearCart();
    
    CartResponse updateQuantity(
            Long productId,
            Integer quantity);
}