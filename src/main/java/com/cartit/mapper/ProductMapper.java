package com.cartit.mapper;

import com.cartit.dto.response.ProductResponse;
import com.cartit.entity.Product;

public final class ProductMapper {

    private ProductMapper() {
    }

    public static ProductResponse toResponse(
            Product product,
            String primaryImageUrl) {

        return new ProductResponse(
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getDescription(),
                CategoryMapper.toResponse(product.getCategory()),
                product.getBrand() != null
                        ? BrandMapper.toResponse(product.getBrand())
                        : null,
                primaryImageUrl,
                product.getUnit(),
                product.getMrp(),
                product.getSellingPrice(),
                product.getStock(),
                product.getMaxPurchaseQuantity(),
                product.getFeatured(),
                product.getActive(),
                product.getUnitQuantity()
        );
    }
}