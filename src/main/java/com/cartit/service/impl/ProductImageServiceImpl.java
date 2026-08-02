package com.cartit.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cartit.dto.request.ProductImageRequest;
import com.cartit.dto.response.ProductImageResponse;
import com.cartit.entity.Product;
import com.cartit.entity.ProductImage;
import com.cartit.exception.ResourceNotFoundException;
import com.cartit.mapper.ProductImageMapper;
import com.cartit.repository.ProductImageRepository;
import com.cartit.repository.ProductRepository;
import com.cartit.service.ProductImageService;
import com.cartit.service.builder.ProductSummaryBuilder;

@Service
public class ProductImageServiceImpl implements ProductImageService {

	private final ProductRepository productRepository;
	private final ProductImageRepository productImageRepository;
	private final ProductSummaryBuilder productSummaryBuilder;

	public ProductImageServiceImpl(
	        ProductRepository productRepository,
	        ProductImageRepository productImageRepository,
	        ProductSummaryBuilder productSummaryBuilder) {

	    this.productRepository = productRepository;
	    this.productImageRepository = productImageRepository;
	    this.productSummaryBuilder = productSummaryBuilder;
	}
	
	private ProductImageResponse buildResponse(ProductImage image) {

	    return ProductImageMapper.toResponse(
	            image,
	            productSummaryBuilder.build(image.getProduct())
	    );
	}

    @Override
    public ProductImageResponse addImage(ProductImageRequest request) {

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        if (Boolean.TRUE.equals(request.getPrimaryImage())) {

            Optional<ProductImage> existingPrimary =
                    productImageRepository
                            .findByProductIdAndPrimaryImageTrue(product.getId());

            existingPrimary.ifPresent(image -> {
                image.setPrimaryImage(false);
                productImageRepository.save(image);
            });
        }

        ProductImage image = new ProductImage();

        image.setProduct(product);
        image.setImageUrl(request.getImageUrl());
        image.setDisplayOrder(request.getDisplayOrder());
        image.setPrimaryImage(
                Boolean.TRUE.equals(request.getPrimaryImage()));

        ProductImage savedImage =
                productImageRepository.save(image);

        return buildResponse(savedImage);
    }

    @Override
    public List<ProductImageResponse> getProductImages(Long productId) {

        return productImageRepository
                .findByProductIdAndActiveTrueOrderByDisplayOrderAsc(productId)
                .stream()
                .map(this::buildResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductImageResponse updateImage(Long id,
                                            ProductImageRequest request) {

        ProductImage image = productImageRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Image not found"));

        if (Boolean.TRUE.equals(request.getPrimaryImage())) {

            Optional<ProductImage> existingPrimary =
                    productImageRepository
                            .findByProductIdAndPrimaryImageTrue(
                                    image.getProduct().getId());

            existingPrimary.ifPresent(primary -> {

                if (!primary.getId().equals(image.getId())) {

                    primary.setPrimaryImage(false);
                    productImageRepository.save(primary);
                }
            });
        }

        image.setImageUrl(request.getImageUrl());
        image.setDisplayOrder(request.getDisplayOrder());
        image.setPrimaryImage(
                Boolean.TRUE.equals(request.getPrimaryImage()));

        ProductImage updated =
                productImageRepository.save(image);

        return buildResponse(updated);
    }

    @Override
    public ProductImageResponse setPrimaryImage(Long id) {

        ProductImage image = productImageRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Image not found"));

        Optional<ProductImage> existingPrimary =
                productImageRepository
                        .findByProductIdAndPrimaryImageTrue(
                                image.getProduct().getId());

        existingPrimary.ifPresent(primary -> {

            if (!primary.getId().equals(image.getId())) {

                primary.setPrimaryImage(false);
                productImageRepository.save(primary);
            }
        });

        image.setPrimaryImage(true);

        ProductImage updated =
                productImageRepository.save(image);

        return buildResponse(updated);
    }

    @Override
    public void deleteImage(Long id) {

        ProductImage image = productImageRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Image not found"));

        productImageRepository.delete(image);
    }
}