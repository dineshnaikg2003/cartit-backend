package com.cartit.mapper;

import com.cartit.dto.response.common.ProductSummaryResponse;
import com.cartit.entity.Product;

public final class ProductSummaryMapper {

    private ProductSummaryMapper() {
    }

    public static ProductSummaryResponse toResponse(
            Product product,
            String primaryImageUrl) {

        return new ProductSummaryResponse(
                product.getId(),
                product.getSku(),
                product.getName(),
                primaryImageUrl,
                product.getMrp(),
                product.getSellingPrice(),
                product.getUnit(),
                product.getUnitQuantity()
        );
    }
}