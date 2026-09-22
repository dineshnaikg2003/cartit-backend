package com.cartit.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cartit.dto.request.ProductRequest;
import com.cartit.dto.request.ProductSearchRequest;
import com.cartit.dto.response.PageResponse;
import com.cartit.dto.response.ProductResponse;
import com.cartit.entity.Brand;
import com.cartit.entity.Category;
import com.cartit.entity.Product;
import com.cartit.entity.ProductImage;
import com.cartit.exception.BadRequestException;
import com.cartit.exception.ResourceNotFoundException;
import com.cartit.repository.BrandRepository;
import com.cartit.repository.CategoryRepository;
import com.cartit.repository.ProductImageRepository;
import com.cartit.repository.ProductRepository;
import com.cartit.service.ProductService;
import com.cartit.service.builder.PageResponseBuilder;
import com.cartit.service.builder.ProductResponseBuilder;
import com.cartit.service.specification.ProductSpecification;
import com.cartit.service.storage.ImageStorageService;
import com.cartit.service.validator.ProductValidator;

@Service
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	private final BrandRepository brandRepository;
	private final ProductResponseBuilder productResponseBuilder;
	private final PageResponseBuilder pageResponseBuilder;
	private final ProductValidator productValidator;
	private final ProductImageRepository productImageRepository;
	private final ImageStorageService imageStorageService;

	public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository,
			BrandRepository brandRepository, ProductResponseBuilder productResponseBuilder,
			PageResponseBuilder pageResponseBuilder, ProductValidator productValidator,
			ProductImageRepository productImageRepository, ImageStorageService imageStorageService) {

		this.imageStorageService = imageStorageService;
		this.productImageRepository = productImageRepository;
		this.productValidator = productValidator;
		this.pageResponseBuilder = pageResponseBuilder;
		this.productResponseBuilder = productResponseBuilder;
		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
		this.brandRepository = brandRepository;
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
		product.setUnitQuantity(request.getUnitQuantity());
		product.setMrp(request.getMrp());
		product.setSellingPrice(request.getSellingPrice());
		product.setStock(request.getStock());
		product.setFeatured(Boolean.TRUE.equals(request.getFeatured()));
		product.setMaxPurchaseQuantity(request.getMaxPurchaseQuantity());

		Product savedProduct = productRepository.save(product);

		return productResponseBuilder.build(savedProduct);
	}

	@Override
	@Transactional(readOnly = true)
	public PageResponse<ProductResponse> getAllProducts(ProductSearchRequest request) {

		productValidator.validateSearch(request);
		Sort sort = request.getDirection().equalsIgnoreCase("asc") ? Sort.by(request.getSortBy()).ascending()
				: Sort.by(request.getSortBy()).descending();

		Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

		Specification<Product> specification = ProductSpecification.search(request);

		Page<Product> products = productRepository.findAll(specification, pageable);

		Page<ProductResponse> response = products.map(productResponseBuilder::build);

		return pageResponseBuilder.build(response);
	}

	@Override
	@Transactional
	public void deleteProduct(Long id) {

		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found."));

		List<ProductImage> images = productImageRepository.findByProductIdAndActiveTrueOrderByDisplayOrderAsc(id);

		for (ProductImage image : images) {
			imageStorageService.deleteProductImage(image.getImageUrl());
		}

		productImageRepository.deleteByProductId(id);

		productRepository.delete(product);
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
		product.setUnitQuantity(request.getUnitQuantity());
		product.setMrp(request.getMrp());
		product.setSellingPrice(request.getSellingPrice());
		product.setStock(request.getStock());
		product.setFeatured(Boolean.TRUE.equals(request.getFeatured()));
		product.setMaxPurchaseQuantity(request.getMaxPurchaseQuantity());

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

	@Override
	@Transactional(readOnly = true)
	public ProductResponse getProductBySku(String sku) {

		Product product = productRepository.findBySku(sku)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found."));

		return productResponseBuilder.build(product);
	}

	@Override
	public ProductResponse markFeatured(Long productId) {

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found"));

		product.setFeatured(true);

		Product updatedProduct = productRepository.save(product);

		return productResponseBuilder.build(updatedProduct);
	}

	@Override
	public ProductResponse removeFeatured(Long productId) {

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found"));

		product.setFeatured(false);

		Product updatedProduct = productRepository.save(product);

		return productResponseBuilder.build(updatedProduct);
	}

	@Override
	public ProductResponse updateStock(Long productId, Integer stock) {

		if (stock < 0) {
			throw new BadRequestException("Stock cannot be negative.");
		}

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found"));

		product.setStock(stock);

		Product updatedProduct = productRepository.save(product);

		return productResponseBuilder.build(updatedProduct);
	}

}