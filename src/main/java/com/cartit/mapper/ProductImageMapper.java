package com.cartit.mapper;

import com.cartit.dto.response.ProductImageResponse;
import com.cartit.dto.response.common.ProductSummaryResponse;
import com.cartit.entity.ProductImage;

public final class ProductImageMapper {

    private ProductImageMapper() {
    }

    public static ProductImageResponse toResponse(
            ProductImage image,
            ProductSummaryResponse product) {

        return new ProductImageResponse(
                image.getId(),
                product,
                image.getImageUrl(),
                image.getDisplayOrder(),
                image.getPrimaryImage(),
                image.getActive()
        );
    }

}