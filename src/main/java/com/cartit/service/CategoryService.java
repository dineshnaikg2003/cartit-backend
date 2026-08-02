package com.cartit.service;

import java.util.List;

import com.cartit.dto.request.CategoryRequest;
import com.cartit.dto.response.CategoryResponse;

public interface CategoryService {

	CategoryResponse createCategory(CategoryRequest request);

	List<CategoryResponse> getAllCategories();

	CategoryResponse getCategoryById(Long id);

	CategoryResponse updateCategory(Long id, CategoryRequest request);

	CategoryResponse activateCategory(Long id);

	CategoryResponse deactivateCategory(Long id);
}