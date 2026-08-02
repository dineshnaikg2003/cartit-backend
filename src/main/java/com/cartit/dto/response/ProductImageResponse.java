package com.cartit.dto.response;

import com.cartit.dto.response.common.ProductSummaryResponse;

public class ProductImageResponse {

    private Long id;

    private ProductSummaryResponse product;

    private String imageUrl;

    private Integer displayOrder;

    private Boolean primaryImage;

    private Boolean active;


	public void setProduct(ProductSummaryResponse product) {
		this.product = product;
	}

	public ProductImageResponse() {
	}

	public ProductImageResponse(Long id,ProductSummaryResponse product, String imageUrl, Integer displayOrder,
			Boolean primaryImage, Boolean active) {

		this.id = id;
		this.product = product;
		this.imageUrl = imageUrl;
		this.displayOrder = displayOrder;
		this.primaryImage = primaryImage;
		this.active = active;
	}

	public ProductSummaryResponse getProduct() {
		return product;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public Boolean getPrimaryImage() {
		return primaryImage;
	}

	public void setPrimaryImage(Boolean primaryImage) {
		this.primaryImage = primaryImage;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

}