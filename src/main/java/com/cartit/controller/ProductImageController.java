package com.cartit.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cartit.dto.request.ProductImageRequest;
import com.cartit.dto.response.ApiResponse;
import com.cartit.dto.response.ProductImageResponse;
import com.cartit.service.ProductImageService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/product-images")
public class ProductImageController {

	private final ProductImageService productImageService;

	public ProductImageController(ProductImageService productImageService) {
		this.productImageService = productImageService;
	}

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse<ProductImageResponse>> addImage(
			@Valid @ModelAttribute ProductImageRequest request) {

		ProductImageResponse response = productImageService.addImage(request);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ApiResponse.success("Product image uploaded successfully.", response));
	}

	@GetMapping("/product/{productId}")
	public ResponseEntity<ApiResponse<List<ProductImageResponse>>> getProductImages(@PathVariable Long productId) {

		List<ProductImageResponse> response = productImageService.getProductImages(productId);

		return ResponseEntity.ok(ApiResponse.success("Product images fetched successfully.", response));
	}

	@PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse<ProductImageResponse>> updateImage(@PathVariable Long id,
			@Valid @ModelAttribute ProductImageRequest request) {

		ProductImageResponse response = productImageService.updateImage(id, request);

		return ResponseEntity.ok(ApiResponse.success("Product image updated successfully.", response));
	}

	@PatchMapping("/{id}/primary")
	public ResponseEntity<ApiResponse<ProductImageResponse>> setPrimaryImage(@PathVariable Long id) {

		ProductImageResponse response = productImageService.setPrimaryImage(id);

		return ResponseEntity.ok(ApiResponse.success("Primary image updated successfully.", response));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> deleteImage(@PathVariable Long id) {

		productImageService.deleteImage(id);

		return ResponseEntity.ok(ApiResponse.success("Product image deleted successfully.", null));
	}
}