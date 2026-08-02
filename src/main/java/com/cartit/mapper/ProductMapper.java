package com.cartit.mapper;

import com.cartit.dto.response.ProductResponse;
import com.cartit.entity.Brand;
import com.cartit.entity.Category;
import com.cartit.entity.Product;

public final class ProductMapper {

    private ProductMapper() {
    }

    public static ProductResponse toResponse(
            Product product,
            String primaryImageUrl) {

        Category category = product.getCategory();
        Brand brand = product.getBrand();

        return new ProductResponse(
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getDescription(),

                CategoryMapper.toResponse(category),

                brand != null
                        ? BrandMapper.toResponse(brand)
                        : null,

                product.getUnit(),
                product.getMrp(),
                product.getSellingPrice(),
                product.getStock(),
                primaryImageUrl,
                product.getAverageRating(),
                product.getFeatured(),
                product.getActive()
        );
    }
}