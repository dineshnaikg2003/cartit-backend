package com.cartit.service.helper;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cartit.entity.Cart;
import com.cartit.entity.CartItem;
import com.cartit.entity.Product;
import com.cartit.entity.User;
import com.cartit.exception.BadRequestException;
import com.cartit.exception.ResourceNotFoundException;
import com.cartit.repository.CartItemRepository;
import com.cartit.repository.CartRepository;
import com.cartit.repository.ProductRepository;
import com.cartit.security.CurrentUserService;

@Service
public class CartHelperImpl implements CartHelper {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final CurrentUserService currentUserService;

    public CartHelperImpl(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            ProductRepository productRepository,
            CurrentUserService currentUserService) {

        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.currentUserService = currentUserService;
    }

    @Override
    public Cart getOrCreateCart() {

        User user = currentUserService.getCurrentUser();

        return cartRepository
                .findByUserIdAndActiveTrue(user.getId())
                .orElseGet(() -> {

                    Cart cart = new Cart();
                    cart.setUser(user);

                    return cartRepository.save(cart);
                });
    }

    @Override
    public Product getProduct(Long productId) {

        return productRepository
                .findByIdAndActiveTrue(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found"));
    }

    @Override
    public Optional<CartItem> getCartItem(
            Cart cart,
            Product product) {

        return cartItemRepository
                .findByCartIdAndProductIdAndActiveTrue(
                        cart.getId(),
                        product.getId());
    }

    @Override
    public CartItem createCartItem(
            Cart cart,
            Product product,
            Integer quantity) {

        CartItem cartItem = new CartItem();

        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        cartItem.setUnitPrice(product.getSellingPrice());

        return cartItemRepository.save(cartItem);
    }

    @Override
    public void validateQuantity(
            Product product,
            Integer quantity) {

        if (quantity < 1) {
            throw new BadRequestException(
                    "Quantity must be at least 1");
        }

        if (quantity > product.getStock()) {
            throw new BadRequestException(
                    "Only " + product.getStock()
                            + " items are available in stock.");
        }

        if (quantity > product.getMaxPurchaseQuantity()) {
            throw new BadRequestException(
                    "Maximum purchase quantity allowed is "
                            + product.getMaxPurchaseQuantity());
        }
    }
}