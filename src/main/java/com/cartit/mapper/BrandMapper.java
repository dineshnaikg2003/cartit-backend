package com.cartit.mapper;

import com.cartit.dto.response.BrandResponse;
import com.cartit.entity.Brand;

public final class BrandMapper {

    private BrandMapper() {
    }

    public static BrandResponse toResponse(Brand brand) {
        return new BrandResponse(
                brand.getId(),
                brand.getName(),
                brand.getDescription(),
                brand.getLogoUrl(),
                brand.getActive()
        );
    }
}