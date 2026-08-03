package com.cartit.service.builder;

import java.util.List;

import org.springframework.stereotype.Component;

import com.cartit.dto.response.CartItemResponse;
import com.cartit.dto.response.CartResponse;
import com.cartit.dto.response.common.ProductSummaryResponse;
import com.cartit.entity.Cart;
import com.cartit.mapper.CartMapper;

@Component
public class CartResponseBuilder {

    private final ProductSummaryBuilder productSummaryBuilder;

    public CartResponseBuilder(
            ProductSummaryBuilder productSummaryBuilder) {

        this.productSummaryBuilder = productSummaryBuilder;
    }

    public CartResponse build(Cart cart) {

        List<CartItemResponse> items = cart.getItems()
                .stream()
                .filter(item -> Boolean.TRUE.equals(item.getActive()))
                .map(item -> {

                    ProductSummaryResponse product =
                            productSummaryBuilder.build(item.getProduct());

                    return CartMapper.toCartItemResponse(
                            item,
                            product);
                })
                .toList();

        return CartMapper.toCartResponse(
                cart,
                items);
    }
}