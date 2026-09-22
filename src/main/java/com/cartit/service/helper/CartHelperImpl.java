package com.cartit.service.helper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cartit.entity.Cart;
import com.cartit.entity.CartItem;
import com.cartit.entity.Offer;
import com.cartit.entity.Product;
import com.cartit.entity.User;
import com.cartit.exception.BadRequestException;
import com.cartit.exception.ResourceNotFoundException;
import com.cartit.repository.CartItemRepository;
import com.cartit.repository.CartRepository;
import com.cartit.repository.OfferRepository;
import com.cartit.repository.ProductRepository;
import com.cartit.security.CurrentUserService;

@Service
public class CartHelperImpl implements CartHelper {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final OfferRepository offerRepository;
    private final CurrentUserService currentUserService;

    public CartHelperImpl(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            ProductRepository productRepository,
            OfferRepository offerRepository,
            CurrentUserService currentUserService) {

        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.offerRepository = offerRepository;
        this.currentUserService = currentUserService;
    }

    private BigDecimal resolveUnitPrice(Cart cart, Product product) {
        LocalDateTime now = LocalDateTime.now();
        List<Offer> activeOffers = offerRepository.findByActiveTrueAndStartDateLessThanEqualAndExpiryDateGreaterThanEqual(now, now);

        for (Offer offer : activeOffers) {
            if (offer.getRewardProduct() != null && offer.getRewardProduct().getId().equals(product.getId())) {
                BigDecimal nonRewardSubtotal = BigDecimal.ZERO;
                for (CartItem item : cart.getItems()) {
                    if (!Boolean.TRUE.equals(item.getActive())) continue;
                    BigDecimal otherPrice = item.getProduct().getSellingPrice();
                    if (item.getProduct().getId().equals(offer.getRewardProduct().getId())) {
                        int regQty = Math.max(0, item.getQuantity() - offer.getRewardQuantity());
                        nonRewardSubtotal = nonRewardSubtotal.add(otherPrice.multiply(BigDecimal.valueOf(regQty)));
                    } else {
                        nonRewardSubtotal = nonRewardSubtotal.add(otherPrice.multiply(BigDecimal.valueOf(item.getQuantity())));
                    }
                }

                if (nonRewardSubtotal.compareTo(offer.getMinimumPurchaseAmount()) >= 0) {
                    return offer.getRewardPrice() != null ? offer.getRewardPrice() : BigDecimal.ZERO;
                }
            }
        }
        return product.getSellingPrice();
    }

    @Override
    public Cart getOrCreateCart() {

        User user = currentUserService.getCurrentUser();

        Cart cart = cartRepository
                .findByUserIdAndActiveTrue(user.getId())
                .orElseGet(() -> {

                    Cart c = new Cart();
                    c.setUser(user);

                    return cartRepository.save(c);
                });

        syncCartPrices(cart);
        return cart;
    }
    
    @Override
    public Cart getActiveCart() {

        User user = currentUserService.getCurrentUser();

        Cart cart = cartRepository
                .findByUserIdAndActiveTrue(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart not found."));

        syncCartPrices(cart);
        return cart;
    }

    @Override
    public void syncCartPrices(Cart cart) {
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            if (cart.getClaimedOffer() != null) {
                cart.setClaimedOffer(null);
                cartRepository.save(cart);
            }
            return;
        }

        Offer claimedOffer = cart.getClaimedOffer();

        if (claimedOffer != null) {
            // Check non-reward subtotal
            BigDecimal nonRewardSubtotal = BigDecimal.ZERO;
            for (CartItem item : cart.getItems()) {
                if (!Boolean.TRUE.equals(item.getActive())) continue;
                BigDecimal itemSellingPrice = item.getProduct().getSellingPrice();
                if (claimedOffer.getRewardProduct() != null
                        && claimedOffer.getRewardProduct().getId().equals(item.getProduct().getId())) {
                    int rewardQty = claimedOffer.getRewardQuantity() != null ? claimedOffer.getRewardQuantity() : 1;
                    int regQty = Math.max(0, item.getQuantity() - rewardQty);
                    nonRewardSubtotal = nonRewardSubtotal.add(itemSellingPrice.multiply(BigDecimal.valueOf(regQty)));
                } else {
                    nonRewardSubtotal = nonRewardSubtotal.add(itemSellingPrice.multiply(BigDecimal.valueOf(item.getQuantity())));
                }
            }

            // If offer is no longer valid or subtotal < minimum order amount
            if (!claimedOffer.isActive() || nonRewardSubtotal.compareTo(claimedOffer.getMinimumPurchaseAmount()) < 0) {
                if (claimedOffer.getRewardProduct() != null) {
                    Product rewardProd = claimedOffer.getRewardProduct();
                    int rewardQty = claimedOffer.getRewardQuantity() != null ? claimedOffer.getRewardQuantity() : 1;
                    for (CartItem item : new java.util.ArrayList<>(cart.getItems())) {
                        if (item.getProduct().getId().equals(rewardProd.getId())) {
                            int rem = item.getQuantity() - rewardQty;
                            if (rem > 0) {
                                item.setQuantity(rem);
                                cartItemRepository.save(item);
                            } else {
                                cart.getItems().remove(item);
                                cartItemRepository.delete(item);
                            }
                            break;
                        }
                    }
                }
                cart.setClaimedOffer(null);
                cartRepository.save(cart);
                claimedOffer = null;
            }
        }

        for (CartItem item : cart.getItems()) {
            if (!Boolean.TRUE.equals(item.getActive())) continue;

            BigDecimal regularPrice = item.getProduct().getSellingPrice();
            BigDecimal unitPrice = regularPrice;

            if (claimedOffer != null && claimedOffer.getRewardProduct() != null
                    && claimedOffer.getRewardProduct().getId().equals(item.getProduct().getId())) {
                int rewardQty = claimedOffer.getRewardQuantity() != null ? claimedOffer.getRewardQuantity() : 1;
                int rQty = Math.min(item.getQuantity(), rewardQty);
                int regQty = item.getQuantity() - rQty;
                BigDecimal rPrice = claimedOffer.getRewardPrice() != null ? claimedOffer.getRewardPrice() : BigDecimal.ZERO;

                BigDecimal totalItemPrice = rPrice.multiply(BigDecimal.valueOf(rQty))
                        .add(regularPrice.multiply(BigDecimal.valueOf(regQty)));

                unitPrice = totalItemPrice.divide(BigDecimal.valueOf(item.getQuantity()), 2, java.math.RoundingMode.HALF_UP);
            }

            item.setUnitPrice(unitPrice);
            cartItemRepository.save(item);
        }
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
        cartItem.setUnitPrice(resolveUnitPrice(cart, product));

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

    @Override
    public void clearCart(Cart cart) {

        cart.getItems()
                .stream()
                .filter(CartItem::getActive)
                .forEach(item -> item.setActive(false));

        cartRepository.save(cart);
    }
}