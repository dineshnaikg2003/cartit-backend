package com.cartit.service.builder;

import java.math.BigDecimal;

import com.cartit.entity.Cart;

public interface AmountCalculator {

    BigDecimal calculateSubTotal(Cart cart);

    BigDecimal calculateDeliveryCharge(Cart cart);

    BigDecimal calculateDiscount(Cart cart);

    BigDecimal calculateTax(Cart cart);

    BigDecimal calculateTotalAmount(
            BigDecimal subTotal,
            BigDecimal deliveryCharge,
            BigDecimal discount,
            BigDecimal tax);
}