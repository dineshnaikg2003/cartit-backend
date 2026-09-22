package com.cartit.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cartit.dto.request.CartRequest;
import com.cartit.dto.request.UpdateCartQuantityRequest;
import com.cartit.dto.response.ApiResponse;
import com.cartit.dto.response.CartResponse;
import com.cartit.service.CartService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CartResponse>> addToCart(
            @Valid @RequestBody CartRequest request) {

        CartResponse response = cartService.addToCart(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        true,
                        "Product added to cart successfully",
                        response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<CartResponse>> getMyCart() {

        CartResponse response = cartService.getMyCart();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Cart fetched successfully",
                        response));
    }

    @PatchMapping("/{productId}/increase")
    public ResponseEntity<ApiResponse<CartResponse>> increaseQuantity(
            @PathVariable Long productId) {

        CartResponse response =
                cartService.increaseQuantity(productId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Quantity increased successfully",
                        response));
    }

    @PatchMapping("/{productId}/decrease")
    public ResponseEntity<ApiResponse<CartResponse>> decreaseQuantity(
            @PathVariable Long productId) {

        CartResponse response =
                cartService.decreaseQuantity(productId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Quantity decreased successfully",
                        response));
    }
    
    @PatchMapping("/{productId}/quantity")
    public ResponseEntity<ApiResponse<CartResponse>> updateQuantity(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateCartQuantityRequest request) {

        CartResponse response = cartService.updateQuantity(
                productId,
                request.getQuantity());

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Quantity updated successfully",
                        response));
    }
    
    

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<CartResponse>> removeFromCart(
            @PathVariable Long productId) {

        CartResponse response =
                cartService.removeFromCart(productId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Product removed from cart successfully",
                        response));
    }

    @PostMapping("/offer/{offerId}")
    public ResponseEntity<ApiResponse<CartResponse>> claimOffer(
            @PathVariable Long offerId) {

        CartResponse response = cartService.claimOffer(offerId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Offer claimed successfully",
                        response));
    }

    @DeleteMapping("/offer")
    public ResponseEntity<ApiResponse<CartResponse>> unclaimOffer() {

        CartResponse response = cartService.unclaimOffer();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Offer unclaimed successfully",
                        response));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<CartResponse>> clearCart() {

        CartResponse response =
                cartService.clearCart();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Cart cleared successfully",
                        response));
    }
}