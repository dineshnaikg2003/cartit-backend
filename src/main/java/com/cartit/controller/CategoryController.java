package com.cartit.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cartit.dto.request.CategoryRequest;
import com.cartit.dto.response.ApiResponse;
import com.cartit.dto.response.CategoryResponse;
import com.cartit.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @Valid @RequestBody CategoryRequest request) {

        CategoryResponse response = categoryService.createCategory(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        true,
                        "Category created successfully",
                        response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories() {

        List<CategoryResponse> response = categoryService.getAllCategories();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Categories fetched successfully",
                        response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(
            @PathVariable Long id) {

        CategoryResponse response = categoryService.getCategoryById(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Category fetched successfully",
                        response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {

        CategoryResponse response =
                categoryService.updateCategory(id, request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Category updated successfully",
                        response));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<CategoryResponse>> activateCategory(
            @PathVariable Long id) {

        CategoryResponse response =
                categoryService.activateCategory(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Category activated successfully",
                        response));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<CategoryResponse>> deactivateCategory(
            @PathVariable Long id) {

        CategoryResponse response =
                categoryService.deactivateCategory(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Category deactivated successfully",
                        response));
    }
}