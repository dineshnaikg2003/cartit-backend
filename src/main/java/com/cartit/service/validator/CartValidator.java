package com.cartit.service.validator;

import org.springframework.stereotype.Component;

import com.cartit.entity.Product;
import com.cartit.exception.BadRequestException;

@Component
public class CartValidator {

    public void validateQuantity(
            Product product,
            Integer quantity) {

        if (quantity == null || quantity < 1) {
            throw new BadRequestException(
                    "Quantity must be at least 1");
        }

        if (quantity > product.getStock()) {
            throw new BadRequestException(
                    "Only "
                            + product.getStock()
                            + " item(s) available in stock.");
        }

        if (quantity > product.getMaxPurchaseQuantity()) {
            throw new BadRequestException(
                    "Maximum purchase quantity allowed is "
                            + product.getMaxPurchaseQuantity());
        }
    }

    public void validateProduct(Product product) {

        if (!Boolean.TRUE.equals(product.getActive())) {
            throw new BadRequestException(
                    "Product is inactive");
        }
    }
}