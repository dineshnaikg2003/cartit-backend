package com.cartit.dto.response;

import java.math.BigDecimal;

import com.cartit.enums.Unit;

public class ProductResponse {

    private Long id;

    private String sku;

    private String name;

    private String description;

    private CategoryResponse category;

    private BrandResponse brand;

    private Unit unit;

    private BigDecimal mrp;

    private BigDecimal sellingPrice;

    private Integer stock;

    private String primaryImageUrl;

    private Double averageRating;

    private Boolean featured;

    private Boolean active;

    public ProductResponse() {
    }

    public ProductResponse(
            Long id,
            String sku,
            String name,
            String description,
            CategoryResponse category,
            BrandResponse brand,
            Unit unit,
            BigDecimal mrp,
            BigDecimal sellingPrice,
            Integer stock,
            String primaryImageUrl,
            Double averageRating,
            Boolean featured,
            Boolean active) {

        this.id = id;
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.unit = unit;
        this.mrp = mrp;
        this.sellingPrice = sellingPrice;
        this.stock = stock;
        this.primaryImageUrl = primaryImageUrl;
        this.averageRating = averageRating;
        this.featured = featured;
        this.active = active;
        this.brand=brand;
        this.category=category;
    }

	public String getPrimaryImageUrl() {
		return primaryImageUrl;
	}

	public void setPrimaryImageUrl(String primaryImageUrl) {
		this.primaryImageUrl = primaryImageUrl;
	}

	public CategoryResponse getCategory() {
		return category;
	}

	public void setCategory(CategoryResponse category) {
		this.category = category;
	}

	public BrandResponse getBrand() {
		return brand;
	}

	public void setBrand(BrandResponse brand) {
		this.brand = brand;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public BigDecimal getMrp() {
		return mrp;
	}

	public void setMrp(BigDecimal mrp) {
		this.mrp = mrp;
	}

	public BigDecimal getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(BigDecimal sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Double getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(Double averageRating) {
		this.averageRating = averageRating;
	}

	public Boolean getFeatured() {
		return featured;
	}

	public void setFeatured(Boolean featured) {
		this.featured = featured;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

    
}