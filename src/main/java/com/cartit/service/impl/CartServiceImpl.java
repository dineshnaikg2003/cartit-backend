package com.cartit.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cartit.dto.request.CartRequest;
import com.cartit.dto.response.CartResponse;
import com.cartit.entity.Cart;
import com.cartit.entity.CartItem;
import com.cartit.entity.Product;
import com.cartit.exception.ResourceNotFoundException;
import com.cartit.repository.CartItemRepository;
import com.cartit.service.CartService;
import com.cartit.service.builder.CartResponseBuilder;
import com.cartit.service.helper.CartHelper;
import com.cartit.service.validator.CartValidator;

import org.springframework.transaction.annotation.Transactional;

@Service
public class CartServiceImpl implements CartService {

	private final CartHelper cartHelper;

	private final CartValidator cartValidator;

	private final CartResponseBuilder cartResponseBuilder;

	private final CartItemRepository cartItemRepository;

	public CartServiceImpl(CartHelper cartHelper, CartValidator cartValidator, CartResponseBuilder cartResponseBuilder,
			CartItemRepository cartItemRepository) {
		this.cartHelper = cartHelper;
		this.cartValidator = cartValidator;
		this.cartResponseBuilder = cartResponseBuilder;
		this.cartItemRepository = cartItemRepository;
	}

	private CartResponse updateCartItemQuantity(Long productId, Integer quantity) {

		Cart cart = cartHelper.getOrCreateCart();

		Product product = cartHelper.getProduct(productId);

		CartItem cartItem = cartHelper.getCartItem(cart, product)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found in cart"));

		cartValidator.validateQuantity(product, quantity);

		cartItem.setQuantity(quantity);

		cartItemRepository.save(cartItem);

		return cartResponseBuilder.build(cart);
	}

	@Override
	@Transactional
	public CartResponse addToCart(CartRequest request) {

		Cart cart = cartHelper.getOrCreateCart();

		Product product = cartHelper.getProduct(request.getProductId());

		cartValidator.validateProduct(product);

		Optional<CartItem> existingCartItem = cartHelper.getCartItem(cart, product);

		if (existingCartItem.isPresent()) {

			CartItem cartItem = existingCartItem.get();

			Integer newQuantity = cartItem.getQuantity() + request.getQuantity();

			cartValidator.validateQuantity(product, newQuantity);

			cartItem.setQuantity(newQuantity);

			cartItemRepository.save(cartItem);

		} else {

			cartValidator.validateQuantity(product, request.getQuantity());

			cartHelper.createCartItem(cart, product, request.getQuantity());
		}

		return cartResponseBuilder.build(cart);
	}

	@Override
	@Transactional(readOnly = true)
	public CartResponse getMyCart() {

		Cart cart = cartHelper.getOrCreateCart();

		return cartResponseBuilder.build(cart);
	}

	@Override
	@Transactional
	public CartResponse increaseQuantity(Long productId) {

		Cart cart = cartHelper.getOrCreateCart();

		Product product = cartHelper.getProduct(productId);

		CartItem cartItem = cartHelper.getCartItem(cart, product)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found in cart"));

		return updateCartItemQuantity(productId, cartItem.getQuantity() + 1);
	}

	@Override
	@Transactional
	public CartResponse decreaseQuantity(Long productId) {

		Cart cart = cartHelper.getOrCreateCart();

		Product product = cartHelper.getProduct(productId);

		CartItem cartItem = cartHelper.getCartItem(cart, product)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found in cart"));

		if (cartItem.getQuantity() == 1) {

			cart.getItems().remove(cartItem);

			cartItemRepository.delete(cartItem);

			return cartResponseBuilder.build(cart);
		}

		return updateCartItemQuantity(productId, cartItem.getQuantity() - 1);
	}

	@Override
	@Transactional
	public CartResponse updateQuantity(Long productId, Integer quantity) {

		return updateCartItemQuantity(productId, quantity);
	}

	@Override
	@Transactional
	public CartResponse removeFromCart(Long productId) {

		Cart cart = cartHelper.getOrCreateCart();

		Product product = cartHelper.getProduct(productId);

		CartItem cartItem = cartHelper.getCartItem(cart, product)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found in cart"));

		cart.getItems().remove(cartItem);
		cartItemRepository.delete(cartItem);

		return cartResponseBuilder.build(cart);
	}

	@Override
	@Transactional
	public CartResponse clearCart() {

		Cart cart = cartHelper.getOrCreateCart();

		cart.getItems().forEach(cartItemRepository::delete);

		cartItemRepository.deleteByCartId(cart.getId());

		cart.getItems().clear();

		return cartResponseBuilder.build(cart);
	}
}