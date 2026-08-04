package com.cartit.service.builder;

import org.springframework.stereotype.Component;

import com.cartit.entity.CartItem;
import com.cartit.entity.Order;
import com.cartit.entity.OrderItem;
import com.cartit.entity.ProductImage;
import com.cartit.repository.ProductImageRepository;

@Component
public class ProductSnapshotBuilderImpl
        implements ProductSnapshotBuilder {

    private final ProductImageRepository productImageRepository;

    public ProductSnapshotBuilderImpl(
            ProductImageRepository productImageRepository) {

        this.productImageRepository = productImageRepository;
    }

    @Override
    public OrderItem build(
            Order order,
            CartItem cartItem) {

        OrderItem item = new OrderItem();

        item.setOrder(order);

        item.setProductId(
                cartItem.getProduct().getId());

        item.setProductSku(
                cartItem.getProduct().getSku());

        item.setProductName(
                cartItem.getProduct().getName());

        String imageUrl = productImageRepository
                .findByProductIdAndPrimaryImageTrueAndActiveTrue(
                        cartItem.getProduct().getId())
                .map(ProductImage::getImageUrl)
                .orElse(null);

        item.setProductImageUrl(imageUrl);

        item.setQuantity(
                cartItem.getQuantity());

        item.setUnitPrice(
                cartItem.getUnitPrice());

        item.setTotalPrice(
                cartItem.getTotalPrice());

        return item;
    }
}