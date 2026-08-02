package com.cartit.service.builder;

import org.springframework.stereotype.Service; 

import com.cartit.dto.response.common.ProductSummaryResponse;
import com.cartit.entity.Product;
import com.cartit.entity.ProductImage;
import com.cartit.mapper.ProductSummaryMapper;
import com.cartit.repository.ProductImageRepository;

@Service
public class ProductSummaryBuilder {

    private final ProductImageRepository productImageRepository;

    public ProductSummaryBuilder(ProductImageRepository productImageRepository) {
        this.productImageRepository = productImageRepository;
    }

    public ProductSummaryResponse build(Product product) {

        String primaryImageUrl = productImageRepository
                .findByProductIdAndPrimaryImageTrueAndActiveTrue(product.getId())
                .map(ProductImage::getImageUrl)
                .orElse(null);

        return ProductSummaryMapper.toResponse(
                product,
                primaryImageUrl
        );
    }
}