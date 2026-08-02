package com.cartit.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cartit.dto.request.WishlistRequest;
import com.cartit.dto.response.WishlistResponse;
import com.cartit.entity.Product;
import com.cartit.entity.User;
import com.cartit.entity.Wishlist;
import com.cartit.exception.BadRequestException;
import com.cartit.exception.ResourceNotFoundException;
import com.cartit.mapper.WishlistMapper;
import com.cartit.repository.ProductRepository;
import com.cartit.repository.WishlistRepository;
import com.cartit.security.CurrentUserService;
import com.cartit.service.WishlistService;
import com.cartit.service.builder.ProductSummaryBuilder;

@Service
public class WishlistServiceImpl implements WishlistService {

	private final WishlistRepository wishlistRepository;
	private final ProductRepository productRepository;
	private final CurrentUserService currentUserService;
	private final ProductSummaryBuilder productSummaryBuilder;

	public WishlistServiceImpl(WishlistRepository wishlistRepository, ProductRepository productRepository,
			CurrentUserService currentUserService, ProductSummaryBuilder productSummaryBuilder) {

		this.wishlistRepository = wishlistRepository;
		this.productRepository = productRepository;
		this.currentUserService = currentUserService;
		this.productSummaryBuilder = productSummaryBuilder;
	}

	private WishlistResponse buildResponse(Wishlist wishlist) {

		return WishlistMapper.toResponse(wishlist, productSummaryBuilder.build(wishlist.getProduct()));
	}

	@Override
	public WishlistResponse addToWishlist(WishlistRequest request) {

		User user = currentUserService.getCurrentUser();

		Product product = productRepository.findById(request.getProductId())
				.orElseThrow(() -> new ResourceNotFoundException("Product not found"));

		if (!Boolean.TRUE.equals(product.getActive())) {
			throw new BadRequestException("Product is inactive");
		}

		if (wishlistRepository.existsByUserIdAndProductId(user.getId(), product.getId())) {

			throw new BadRequestException("Product already exists in wishlist");
		}

		Wishlist wishlist = new Wishlist();

		wishlist.setUser(user);
		wishlist.setProduct(product);

		Wishlist savedWishlist = wishlistRepository.save(wishlist);

		return buildResponse(savedWishlist);
	}

	@Override
	public List<WishlistResponse> getMyWishlist() {

		User user = currentUserService.getCurrentUser();

		return wishlistRepository.findByUserIdOrderByCreatedAtDesc(user.getId()).stream()
				.map(this::buildResponse).collect(Collectors.toList());
	}

	@Override
	public void removeFromWishlist(Long productId) {

		User user = currentUserService.getCurrentUser();

		Wishlist wishlist = wishlistRepository.findByUserIdAndProductId(user.getId(), productId)
				.orElseThrow(() -> new ResourceNotFoundException("Wishlist item not found"));

		wishlistRepository.delete(wishlist);
	}

	@Override
	public boolean isWishlisted(Long productId) {

		User user = currentUserService.getCurrentUser();

		return wishlistRepository.existsByUserIdAndProductId(user.getId(), productId);
	}

}