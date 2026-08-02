package com.cartit.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cartit.dto.request.WishlistRequest;
import com.cartit.dto.response.ApiResponse;
import com.cartit.dto.response.WishlistResponse;
import com.cartit.service.WishlistService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<WishlistResponse>> addToWishlist(
            @Valid @RequestBody WishlistRequest request) {

        WishlistResponse response =
                wishlistService.addToWishlist(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        true,
                        "Product added to wishlist",
                        response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<WishlistResponse>>> getMyWishlist() {

        List<WishlistResponse> response =
                wishlistService.getMyWishlist();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Wishlist fetched successfully",
                        response));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<Void>> removeFromWishlist(
            @PathVariable Long productId) {

        wishlistService.removeFromWishlist(productId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Product removed from wishlist",
                        null));
    }

    @GetMapping("/check/{productId}")
    public ResponseEntity<ApiResponse<Boolean>> isWishlisted(
            @PathVariable Long productId) {

        Boolean response =
                wishlistService.isWishlisted(productId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Wishlist status fetched successfully",
                        response));
    }
}