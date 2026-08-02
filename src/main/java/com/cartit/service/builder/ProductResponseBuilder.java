package com.cartit.service.builder;

import org.springframework.stereotype.Service;

import com.cartit.dto.response.ProductResponse;
import com.cartit.entity.Product;
import com.cartit.entity.ProductImage;
import com.cartit.mapper.ProductMapper;
import com.cartit.repository.ProductImageRepository;

@Service
public class ProductResponseBuilder {

    private final ProductImageRepository productImageRepository;

    public ProductResponseBuilder(
            ProductImageRepository productImageRepository) {

        this.productImageRepository = productImageRepository;
    }

    public ProductResponse build(Product product) {

        String primaryImageUrl = productImageRepository
                .findByProductIdAndPrimaryImageTrueAndActiveTrue(product.getId())
                .map(ProductImage::getImageUrl)
                .orElse(null);

        return ProductMapper.toResponse(product, primaryImageUrl);
    }
}
