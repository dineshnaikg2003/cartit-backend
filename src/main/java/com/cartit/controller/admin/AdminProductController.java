package com.cartit.controller.admin;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cartit.dto.request.ProductRequest;
import com.cartit.dto.request.ProductSearchRequest;
import com.cartit.dto.response.ApiResponse;
import com.cartit.dto.response.PageResponse;
import com.cartit.dto.response.ProductResponse;
import com.cartit.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {

	private final ProductService productService;

	public AdminProductController(ProductService productService) {
		this.productService = productService;
	}

	@PostMapping
	public ResponseEntity<ApiResponse<ProductResponse>> createProduct(@Valid @RequestBody ProductRequest request) {

		ProductResponse response = productService.createProduct(request);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ApiResponse.success("Product created successfully.", response));
	}

	@PatchMapping("/{id}/stock")
	public ResponseEntity<ApiResponse<ProductResponse>> updateStock(
	        @PathVariable Long id,
	        @RequestParam Integer stock) {

	    ProductResponse response =
	            productService.updateStock(id, stock);

	    return ResponseEntity.ok(
	            ApiResponse.success(
	                    "Product stock updated successfully.",
	                    response
	            )
	    );
	}

	@GetMapping("/sku/{sku}")
	public ResponseEntity<ApiResponse<ProductResponse>> getProductBySku(@PathVariable String sku) {

		ProductResponse response = productService.getProductBySku(sku);

		return ResponseEntity.ok(ApiResponse.success("Product fetched successfully.", response));
	}

	@GetMapping
	public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> getAllProducts(
			@ModelAttribute ProductSearchRequest request) {

		PageResponse<ProductResponse> response = productService.getAllProducts(request);

		return ResponseEntity.ok(ApiResponse.success("Products fetched successfully.", response));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<ProductResponse>> getProduct(
	        @PathVariable Long id) {

	    ProductResponse response =
	            productService.getProductById(id);

	    return ResponseEntity.ok(
	            ApiResponse.success(
	                    "Product fetched successfully.",
	                    response));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(@PathVariable Long id,
			@Valid @RequestBody ProductRequest request) {

		ProductResponse response = productService.updateProduct(id, request);

		return ResponseEntity.ok(ApiResponse.success("Product updated successfully.", response));
	}

	@PatchMapping("/{id}/activate")
	public ResponseEntity<ApiResponse<ProductResponse>> activateProduct(@PathVariable Long id) {

		ProductResponse response = productService.activateProduct(id);

		return ResponseEntity.ok(ApiResponse.success("Product activated successfully.", response));
	}

	@PatchMapping("/{id}/deactivate")
	public ResponseEntity<ApiResponse<ProductResponse>> deactivateProduct(@PathVariable Long id) {

		ProductResponse response = productService.deactivateProduct(id);

		return ResponseEntity.ok(ApiResponse.success("Product deactivated successfully.", response));
	}

	@PatchMapping("/{id}/featured")
	public ResponseEntity<ApiResponse<ProductResponse>> markFeatured(@PathVariable Long id) {

		ProductResponse response = productService.markFeatured(id);

		return ResponseEntity.ok(ApiResponse.success("Product marked as featured successfully.", response));
	}

	@PatchMapping("/{id}/unfeatured")
	public ResponseEntity<ApiResponse<ProductResponse>> removeFeatured(@PathVariable Long id) {

		ProductResponse response = productService.removeFeatured(id);

		return ResponseEntity.ok(ApiResponse.success("Product removed from featured successfully.", response));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {

		productService.deleteProduct(id);

		return ResponseEntity.ok(ApiResponse.success("Product deleted successfully.", null));
	}

}
