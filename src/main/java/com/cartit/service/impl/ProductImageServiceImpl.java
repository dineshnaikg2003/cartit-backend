package com.cartit.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cartit.dto.request.ProductImageReorderRequest;
import com.cartit.dto.request.ProductImageRequest;
import com.cartit.dto.request.ProductImageUpdateRequest;
import com.cartit.dto.response.ProductImageResponse;
import com.cartit.entity.Product;
import com.cartit.entity.ProductImage;
import com.cartit.exception.ResourceNotFoundException;
import com.cartit.mapper.ProductImageMapper;
import com.cartit.repository.ProductImageRepository;
import com.cartit.repository.ProductRepository;
import com.cartit.service.ProductImageService;
import com.cartit.service.builder.ProductSummaryBuilder;
import com.cartit.service.storage.ImageStorageService;

@Service
public class ProductImageServiceImpl implements ProductImageService {

	private final ProductRepository productRepository;
	private final ProductImageRepository productImageRepository;
	private final ProductSummaryBuilder productSummaryBuilder;
	private final ImageStorageService imageStorageService;

	public ProductImageServiceImpl(ProductRepository productRepository, ProductImageRepository productImageRepository,
			ProductSummaryBuilder productSummaryBuilder, ImageStorageService imageStorageService) {

		this.productRepository = productRepository;
		this.productImageRepository = productImageRepository;
		this.productSummaryBuilder = productSummaryBuilder;
		this.imageStorageService = imageStorageService;
	}

	private ProductImageResponse buildResponse(ProductImage image) {

		return ProductImageMapper.toResponse(image, productSummaryBuilder.build(image.getProduct()));
	}

	private void shiftDisplayOrder(Long productId, Integer displayOrder) {

		List<ProductImage> images = productImageRepository
				.findByProductIdAndDisplayOrderGreaterThanEqualAndActiveTrueOrderByDisplayOrderAsc(productId,
						displayOrder);

		for (ProductImage image : images) {

			image.setDisplayOrder(image.getDisplayOrder() + 1);
		}

		productImageRepository.saveAll(images);
	}

	@Override
	public ProductImageResponse addImage(ProductImageRequest request) {

		Product product = productRepository.findById(request.getProductId())
				.orElseThrow(() -> new ResourceNotFoundException("Product not found"));

		shiftDisplayOrder(product.getId(), request.getDisplayOrder());

		if (Boolean.TRUE.equals(request.getPrimaryImage())) {

			productImageRepository.findByProductIdAndPrimaryImageTrue(product.getId()).ifPresent(image -> {

				image.setPrimaryImage(false);
				productImageRepository.save(image);
			});
		}

		String imageUrl = imageStorageService.uploadProductImage(request.getImage());

		ProductImage image = new ProductImage();

		image.setProduct(product);
		image.setImageUrl(imageUrl);
		image.setDisplayOrder(request.getDisplayOrder());
		image.setPrimaryImage(Boolean.TRUE.equals(request.getPrimaryImage()));

		ProductImage savedImage = productImageRepository.save(image);

		return buildResponse(savedImage);
	}

	@Override
	public List<ProductImageResponse> getProductImages(Long productId) {

		return productImageRepository.findByProductIdAndActiveTrueOrderByDisplayOrderAsc(productId).stream()
				.map(this::buildResponse).collect(Collectors.toList());
	}

	@Override
	public ProductImageResponse updateImage(Long id, ProductImageUpdateRequest request) {

		ProductImage image = productImageRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Image not found"));

		shiftDisplayOrder(image.getProduct().getId(), request.getDisplayOrder());

		if (Boolean.TRUE.equals(request.getPrimaryImage())) {

			productImageRepository.findByProductIdAndPrimaryImageTrue(image.getProduct().getId()).ifPresent(primary -> {

				if (!primary.getId().equals(image.getId())) {

					primary.setPrimaryImage(false);
					productImageRepository.save(primary);
				}
			});
		}

		imageStorageService.deleteProductImage(image.getImageUrl());

		String imageUrl = imageStorageService.uploadProductImage(request.getImage());

		image.setImageUrl(imageUrl);
		image.setDisplayOrder(request.getDisplayOrder());
		image.setPrimaryImage(Boolean.TRUE.equals(request.getPrimaryImage()));

		ProductImage updated = productImageRepository.save(image);

		return buildResponse(updated);
	}

	@Override
	public void reorderImages(List<ProductImageReorderRequest> requests) {

	    List<ProductImage> images = new ArrayList<>();

	    for (ProductImageReorderRequest request : requests) {

	        ProductImage image =
	                productImageRepository.findById(request.getId())
	                        .orElseThrow(() ->
	                                new ResourceNotFoundException("Image not found"));

	        image.setDisplayOrder(request.getDisplayOrder());

	        images.add(image);
	    }

	    productImageRepository.saveAll(images);
	}
	@Override
	public ProductImageResponse setPrimaryImage(Long id) {

		ProductImage image = productImageRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Image not found"));

		productImageRepository.findByProductIdAndPrimaryImageTrue(image.getProduct().getId()).ifPresent(primary -> {

			if (!primary.getId().equals(image.getId())) {

				primary.setPrimaryImage(false);
				productImageRepository.save(primary);
			}
		});

		image.setPrimaryImage(true);

		return buildResponse(productImageRepository.save(image));
	}

	@Override
	public void deleteImage(Long id) {

		ProductImage image = productImageRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Image not found"));

		imageStorageService.deleteProductImage(image.getImageUrl());

		productImageRepository.delete(image);
	}
}