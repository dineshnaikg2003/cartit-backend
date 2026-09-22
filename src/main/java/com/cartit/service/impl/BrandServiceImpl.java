package com.cartit.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cartit.dto.request.BrandRequest;
import com.cartit.dto.response.BrandResponse;
import com.cartit.entity.Brand;
import com.cartit.exception.BadRequestException;
import com.cartit.exception.ResourceNotFoundException;
import com.cartit.mapper.BrandMapper;
import com.cartit.repository.BrandRepository;
import com.cartit.service.BrandService;

@Service
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;

    public BrandServiceImpl(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public BrandResponse createBrand(BrandRequest request) {

        if (brandRepository.existsByName(request.getName().trim())) {
            throw new BadRequestException("Brand already exists");
        }

        Brand brand = new Brand();
        brand.setName(request.getName().trim());
        brand.setDescription(request.getDescription());
        brand.setLogoUrl(request.getLogoUrl());

        Brand savedBrand = brandRepository.save(brand);

        return BrandMapper.toResponse(savedBrand);
    }

    @Override
    public List<BrandResponse> getAllBrands() {

        return brandRepository.findByActiveTrueOrderByNameAsc()
                .stream()
                .map(BrandMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BrandResponse getBrandById(Long id) {

        Brand brand = brandRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Brand not found"));

        return BrandMapper.toResponse(brand);
    }

    @Override
    public List<BrandResponse> getAllBrandsForAdmin() {

        return brandRepository.findAll()
                .stream()
                .sorted(
                    java.util.Comparator.comparing(
                        Brand::getName,
                        String.CASE_INSENSITIVE_ORDER
                    )
                )
                .map(BrandMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BrandResponse updateBrand(Long id, BrandRequest request) {

        Brand brand = brandRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Brand not found"));

        if (!brand.getName().equalsIgnoreCase(request.getName().trim())
                && brandRepository.existsByName(request.getName().trim())) {

            throw new BadRequestException("Brand already exists");
        }

        brand.setName(request.getName().trim());
        brand.setDescription(request.getDescription());
        brand.setLogoUrl(request.getLogoUrl());

        Brand updatedBrand = brandRepository.save(brand);

        return BrandMapper.toResponse(updatedBrand);
    }

    @Override
    public BrandResponse activateBrand(Long id) {

        Brand brand = brandRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Brand not found"));

        brand.setActive(true);

        Brand savedBrand = brandRepository.save(brand);
        return BrandMapper.toResponse(savedBrand);
    }

    @Override
    public BrandResponse deactivateBrand(Long id) {

        Brand brand = brandRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Brand not found"));

        brand.setActive(false);
        Brand savedBrand=brandRepository.save(brand);
        return BrandMapper.toResponse(savedBrand);
    }

}
