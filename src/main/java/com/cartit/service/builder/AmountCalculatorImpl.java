package com.cartit.service.builder;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.cartit.entity.Cart;

@Component
public class AmountCalculatorImpl implements AmountCalculator {

	@Override
	public BigDecimal calculateSubTotal(Cart cart) {

		return cart.getSubTotal();
	}

	@Override
	public BigDecimal calculateDeliveryCharge(Cart cart) {

		BigDecimal subTotal = cart.getSubTotal();

		// Free delivery above ₹499
		if (subTotal.compareTo(new BigDecimal("499")) >= 0) {
			return BigDecimal.ZERO;
		}

		return new BigDecimal("40");
	}

	@Override
	public BigDecimal calculateDiscount(Cart cart) {

		// Future:
		// Coupon
		// Membership
		// Festival Offer

		return BigDecimal.ZERO;
	}

	@Override
	public BigDecimal calculateTax(Cart cart) {

		// GST calculation can be added later

		return BigDecimal.ZERO;
	}

	@Override
	public BigDecimal calculateTotalAmount(BigDecimal subTotal, BigDecimal deliveryCharge, BigDecimal discount,
			BigDecimal tax) {

		return subTotal.add(deliveryCharge).add(tax).subtract(discount);
	}
}