package com.cartit.dto.request;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;

public class ProductImageUpdateRequest {

    @NotNull(message = "Image is required")
    private MultipartFile image;

    @NotNull(message = "Display order is required")
    private Integer displayOrder;

    @NotNull(message = "Primary image flag is required")
    private Boolean primaryImage;

    public ProductImageUpdateRequest() {
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
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