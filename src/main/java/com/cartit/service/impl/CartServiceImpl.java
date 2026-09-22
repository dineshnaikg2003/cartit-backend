package com.cartit.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cartit.dto.request.CartRequest;
import com.cartit.dto.response.CartResponse;
import com.cartit.entity.Cart;
import com.cartit.entity.CartItem;
import com.cartit.entity.Offer;
import com.cartit.entity.Product;
import com.cartit.exception.ResourceNotFoundException;
import com.cartit.repository.CartItemRepository;
import com.cartit.repository.CartRepository;
import com.cartit.repository.OfferRepository;
import com.cartit.service.CartService;
import com.cartit.service.builder.CartResponseBuilder;
import com.cartit.service.helper.CartHelper;
import com.cartit.service.validator.CartValidator;

@Service
public class CartServiceImpl implements CartService {

	private final CartHelper cartHelper;

	private final CartValidator cartValidator;

	private final CartResponseBuilder cartResponseBuilder;

	private final CartItemRepository cartItemRepository;

	private final OfferRepository offerRepository;

	private final CartRepository cartRepository;

	public CartServiceImpl(CartHelper cartHelper, CartValidator cartValidator, CartResponseBuilder cartResponseBuilder,
			CartItemRepository cartItemRepository, OfferRepository offerRepository, CartRepository cartRepository) {
		this.cartHelper = cartHelper;
		this.cartValidator = cartValidator;
		this.cartResponseBuilder = cartResponseBuilder;
		this.cartItemRepository = cartItemRepository;
		this.offerRepository = offerRepository;
		this.cartRepository = cartRepository;
	}

	private CartResponse updateCartItemQuantity(Long productId, Integer quantity) {

		Cart cart = cartHelper.getOrCreateCart();

		Product product = cartHelper.getProduct(productId);

		CartItem cartItem = cartHelper.getCartItem(cart, product)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found in cart"));

		if (quantity == null || quantity <= 0) {
			if (cart.getClaimedOffer() != null && cart.getClaimedOffer().getRewardProduct() != null
					&& cart.getClaimedOffer().getRewardProduct().getId().equals(productId)) {
				cart.setClaimedOffer(null);
				cartRepository.save(cart);
			}
			cart.getItems().remove(cartItem);
			cartItemRepository.delete(cartItem);
			cartHelper.syncCartPrices(cart);
			return cartResponseBuilder.build(cart);
		}

		cartValidator.validateQuantity(product, quantity);

		cartItem.setQuantity(quantity);

		cartItemRepository.save(cartItem);

		cartHelper.syncCartPrices(cart);

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

		cartHelper.syncCartPrices(cart);

		return cartResponseBuilder.build(cart);
	}

	@Override
	@Transactional
	public CartResponse getMyCart() {

		Cart cart = cartHelper.getOrCreateCart();

		cartHelper.syncCartPrices(cart);

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

		if (cartItem.getQuantity() <= 1) {

			if (cart.getClaimedOffer() != null && cart.getClaimedOffer().getRewardProduct() != null
					&& cart.getClaimedOffer().getRewardProduct().getId().equals(productId)) {
				cart.setClaimedOffer(null);
				cartRepository.save(cart);
			}

			cart.getItems().remove(cartItem);

			cartItemRepository.delete(cartItem);

			cartHelper.syncCartPrices(cart);

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
	public CartResponse claimOffer(Long offerId) {
		Cart cart = cartHelper.getOrCreateCart();
		Offer offer = offerRepository.findById(offerId)
				.orElseThrow(() -> new ResourceNotFoundException("Offer not found with id: " + offerId));

		// If another offer was previously claimed, unclaim its reward quantity first
		Offer oldOffer = cart.getClaimedOffer();
		if (oldOffer != null && !oldOffer.getId().equals(offerId) && oldOffer.getRewardProduct() != null) {
			Product oldRewardProd = oldOffer.getRewardProduct();
			Optional<CartItem> oldExisting = cartHelper.getCartItem(cart, oldRewardProd);
			if (oldExisting.isPresent()) {
				CartItem oldItem = oldExisting.get();
				int oldRewardQty = oldOffer.getRewardQuantity() != null ? oldOffer.getRewardQuantity() : 1;
				int rem = oldItem.getQuantity() - oldRewardQty;
				if (rem > 0) {
					oldItem.setQuantity(rem);
					cartItemRepository.save(oldItem);
				} else {
					cart.getItems().remove(oldItem);
					cartItemRepository.delete(oldItem);
				}
			}
		}

		cart.setClaimedOffer(offer);

		if (offer.getRewardProduct() != null) {
			Product rewardProd = offer.getRewardProduct();
			int rewardQty = offer.getRewardQuantity() != null ? offer.getRewardQuantity() : 1;
			Optional<CartItem> existing = cartHelper.getCartItem(cart, rewardProd);
			if (existing.isPresent()) {
				CartItem item = existing.get();
				item.setQuantity(item.getQuantity() + rewardQty);
				cartItemRepository.save(item);
			} else {
				cartHelper.createCartItem(cart, rewardProd, rewardQty);
			}
		}

		cartRepository.save(cart);
		cartHelper.syncCartPrices(cart);
		return cartResponseBuilder.build(cart);
	}

	@Override
	@Transactional
	public CartResponse unclaimOffer() {
		Cart cart = cartHelper.getOrCreateCart();
		Offer oldOffer = cart.getClaimedOffer();
		if (oldOffer != null && oldOffer.getRewardProduct() != null) {
			Product rewardProd = oldOffer.getRewardProduct();
			Optional<CartItem> existing = cartHelper.getCartItem(cart, rewardProd);
			if (existing.isPresent()) {
				CartItem item = existing.get();
				int rewardQty = oldOffer.getRewardQuantity() != null ? oldOffer.getRewardQuantity() : 1;
				int rem = item.getQuantity() - rewardQty;
				if (rem > 0) {
					item.setQuantity(rem);
					cartItemRepository.save(item);
				} else {
					cart.getItems().remove(item);
					cartItemRepository.delete(item);
				}
			}
		}

		cart.setClaimedOffer(null);
		cartRepository.save(cart);
		cartHelper.syncCartPrices(cart);
		return cartResponseBuilder.build(cart);
	}

	@Override
	@Transactional
	public CartResponse removeFromCart(Long productId) {

		Cart cart = cartHelper.getOrCreateCart();

		Product product = cartHelper.getProduct(productId);

		CartItem cartItem = cartHelper.getCartItem(cart, product)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found in cart"));

		if (cart.getClaimedOffer() != null && cart.getClaimedOffer().getRewardProduct() != null
				&& cart.getClaimedOffer().getRewardProduct().getId().equals(productId)) {
			cart.setClaimedOffer(null);
			cartRepository.save(cart);
		}

		cart.getItems().remove(cartItem);
		cartItemRepository.delete(cartItem);

		cartHelper.syncCartPrices(cart);

		return cartResponseBuilder.build(cart);
	}

	@Override
	@Transactional
	public CartResponse clearCart() {

		Cart cart = cartHelper.getOrCreateCart();

		cart.setClaimedOffer(null);
		cartRepository.save(cart);

		cartItemRepository.deleteByCartId(cart.getId());

		cart.getItems().clear();

		cartHelper.syncCartPrices(cart);

		return cartResponseBuilder.build(cart);
	}
}