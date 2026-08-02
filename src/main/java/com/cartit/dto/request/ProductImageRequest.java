package com.cartit.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProductImageRequest {

    @NotNull(message = "Product is required")
    private Long productId;

    @NotBlank(message = "Image URL is required")
    private String imageUrl;

    @Min(value = 1, message = "Display order must be at least 1")
    private Integer displayOrder = 1;

    private Boolean primaryImage = false;

    public ProductImageRequest() {
    }

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
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

}