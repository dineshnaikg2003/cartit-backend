package com.cartit.service;

import java.util.List;

import com.cartit.dto.request.ProductImageRequest;
import com.cartit.dto.response.ProductImageResponse;

public interface ProductImageService {

	ProductImageResponse addImage(ProductImageRequest request);

	List<ProductImageResponse> getProductImages(Long productId);

	ProductImageResponse updateImage(Long id, ProductImageRequest request);

	ProductImageResponse setPrimaryImage(Long id);

	void deleteImage(Long id);

}