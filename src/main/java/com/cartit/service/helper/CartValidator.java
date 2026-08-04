package com.cartit.service.helper;

import com.cartit.entity.Address;
import com.cartit.entity.Cart;
import com.cartit.entity.CartItem;
import com.cartit.exception.BadRequestException;

public class CartValidator {
	
	public void validateCheckout(Cart cart, Address address) {

		if (cart.getItems().stream().noneMatch(CartItem::getActive)) {

			throw new BadRequestException("Your cart is empty.");
		}

		if (!Boolean.TRUE.equals(address.getActive())) {

			throw new BadRequestException("Selected address is inactive.");
		}
	}
}
