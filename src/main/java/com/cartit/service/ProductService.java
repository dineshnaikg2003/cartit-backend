package com.cartit.service;

import java.util.List;

import com.cartit.dto.request.ProductRequest;
import com.cartit.dto.response.ProductResponse;

public interface ProductService {

    ProductResponse createProduct(ProductRequest request);

    List<ProductResponse> getAllProducts();

    ProductResponse getProductById(Long id);

    ProductResponse updateProduct(Long id, ProductRequest request);

    ProductResponse activateProduct(Long id);

    ProductResponse deactivateProduct(Long id);

    List<ProductResponse> getProductsByCategory(Long categoryId);

    List<ProductResponse> getProductsByBrand(Long brandId);

    List<ProductResponse> getFeaturedProducts();
}