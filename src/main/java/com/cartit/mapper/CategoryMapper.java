package com.cartit.mapper;

import com.cartit.dto.response.CategoryResponse;
import com.cartit.entity.Category;

public class CategoryMapper {

    private CategoryMapper() {
    }

    public static CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getImageUrl(),
                category.getDisplayOrder(),
                category.getActive()
        );
    }
}