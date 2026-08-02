package com.cartit.service;

import java.util.List;

import com.cartit.dto.request.BrandRequest;
import com.cartit.dto.response.BrandResponse;

public interface BrandService {

    BrandResponse createBrand(BrandRequest request);

    List<BrandResponse> getAllBrands();

    BrandResponse getBrandById(Long id);

    BrandResponse updateBrand(Long id, BrandRequest request);

    BrandResponse activateBrand(Long id);

    BrandResponse deactivateBrand(Long id);
}