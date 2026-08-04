package com.cartit.service.helper;

import java.util.Optional;

import com.cartit.entity.Cart;
import com.cartit.entity.CartItem;
import com.cartit.entity.Product;

public interface CartHelper {

    Cart getOrCreateCart();

    Product getProduct(Long productId);

    Optional<CartItem> getCartItem(
            Cart cart,
            Product product);

    CartItem createCartItem(
            Cart cart,
            Product product,
            Integer quantity);

    void validateQuantity(
            Product product,
            Integer quantity);
    
    Cart getActiveCart();
    
    void clearCart(Cart cart);
    
}