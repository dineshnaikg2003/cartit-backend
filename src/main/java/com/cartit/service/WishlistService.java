package com.cartit.service;

import java.util.List;

import com.cartit.dto.request.WishlistRequest;
import com.cartit.dto.response.WishlistResponse;

public interface WishlistService {

    WishlistResponse addToWishlist(WishlistRequest request);

    List<WishlistResponse> getMyWishlist();

    void removeFromWishlist(Long productId);

    boolean isWishlisted(Long productId);

}