package com.cartit.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cartit.dto.request.ProductRequest;
import com.cartit.dto.response.ProductResponse;
import com.cartit.entity.Brand;
import com.cartit.entity.Category;
import com.cartit.entity.Product;
import com.cartit.exception.BadRequestException;
import com.cartit.exception.ResourceNotFoundException;
import com.cartit.repository.BrandRepository;
import com.cartit.repository.CategoryRepository;
import com.cartit.repository.ProductImageRepository;
import com.cartit.repository.ProductRepository;
import com.cartit.service.ProductService;
import com.cartit.service.builder.ProductResponseBuilder;

@Service
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	private final BrandRepository brandRepository;
	private final ProductImageRepository productImageRepository;

	private final ProductResponseBuilder productResponseBuilder;

	public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository,
			BrandRepository brandRepository, ProductImageRepository productImageRepository,
			ProductResponseBuilder productResponseBuilder) {
		this.productResponseBuilder = productResponseBuilder;
		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
		this.brandRepository = brandRepository;
		this.productImageRepository = productImageRepository;
	}

	@Override
	public ProductResponse createProduct(ProductRequest request) {

		Category category = categoryRepository.findById(request.getCategoryId())
				.orElseThrow(() -> new ResourceNotFoundException("Category not found"));

		Brand brand = null;

		if (request.getBrandId() != null) {
			brand = brandRepository.findById(request.getBrandId())
					.orElseThrow(() -> new ResourceNotFoundException("Brand not found"));
		}

		if (request.getSellingPrice().compareTo(request.getMrp()) > 0) {
			throw new BadRequestException("Selling price cannot be greater than MRP");
		}

		Product product = new Product();

		product.setSku(generateSku(request.getName()));
		product.setName(request.getName().trim());
		product.setDescription(request.getDescription());
		product.setCategory(category);
		product.setBrand(brand);
		product.setUnit(request.getUnit());
		product.setMrp(request.getMrp());
		product.setSellingPrice(request.getSellingPrice());
		product.setStock(request.getStock());
		product.setFeatured(Boolean.TRUE.equals(request.getFeatured()));

		Product savedProduct = productRepository.save(product);

		return productResponseBuilder.build(savedProduct);
	}

	@Override
	public List<ProductResponse> getAllProducts() {

		return productRepository.findByActiveTrueOrderByNameAsc().stream().map(productResponseBuilder::build).toList();
	}

	@Override
	public ProductResponse getProductById(Long id) {

		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found"));

		return productResponseBuilder.build(product);
	}

	@Override
	public ProductResponse updateProduct(Long id, ProductRequest request) {

		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found"));

		Category category = categoryRepository.findById(request.getCategoryId())
				.orElseThrow(() -> new ResourceNotFoundException("Category not found"));

		Brand brand = null;

		if (request.getBrandId() != null) {
			brand = brandRepository.findById(request.getBrandId())
					.orElseThrow(() -> new ResourceNotFoundException("Brand not found"));
		}

		if (request.getSellingPrice().compareTo(request.getMrp()) > 0) {
			throw new BadRequestException("Selling price cannot be greater than MRP");
		}

		product.setName(request.getName().trim());
		product.setDescription(request.getDescription());
		product.setCategory(category);
		product.setBrand(brand);
		product.setUnit(request.getUnit());
		product.setMrp(request.getMrp());
		product.setSellingPrice(request.getSellingPrice());
		product.setStock(request.getStock());
		product.setFeatured(Boolean.TRUE.equals(request.getFeatured()));

		Product updatedProduct = productRepository.save(product);

		return productResponseBuilder.build(updatedProduct);
	}

	@Override
	public ProductResponse activateProduct(Long id) {

		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found"));

		product.setActive(true);

		Product updatedProduct = productRepository.save(product);

		return productResponseBuilder.build(updatedProduct);
	}

	@Override
	public ProductResponse deactivateProduct(Long id) {

		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found"));

		product.setActive(false);

		Product updatedProduct = productRepository.save(product);

		return productResponseBuilder.build(updatedProduct);
	}

	@Override
	public List<ProductResponse> getProductsByCategory(Long categoryId) {

		return productRepository.findByCategoryIdAndActiveTrue(categoryId).stream().map(productResponseBuilder::build)
				.toList();
	}

	@Override
	public List<ProductResponse> getProductsByBrand(Long brandId) {

		return productRepository.findByBrandIdAndActiveTrue(brandId).stream().map(productResponseBuilder::build)
				.toList();
	}

	@Override
	public List<ProductResponse> getFeaturedProducts() {

		return productRepository.findByFeaturedTrueAndActiveTrue().stream().map(productResponseBuilder::build).toList();
	}

	private String generateSku(String productName) {

		String prefix = productName.trim().replaceAll("[^A-Za-z]", "").toUpperCase();

		if (prefix.length() >= 3) {
			prefix = prefix.substring(0, 3);
		} else {
			while (prefix.length() < 3) {
				prefix += "X";
			}
		}

		int counter = 1;
		String sku;

		do {
			sku = prefix + String.format("%03d", counter++);
		} while (productRepository.existsBySku(sku));

		return sku;
	}
}