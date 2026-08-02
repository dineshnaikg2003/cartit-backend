package com.cartit.dto.response.common;

import java.math.BigDecimal;

public class ProductSummaryResponse {

    private Long id;

    private String sku;

    private String name;

    private String thumbnail;

    private BigDecimal sellingPrice;

    public ProductSummaryResponse() {
    }

    public ProductSummaryResponse(
            Long id,
            String sku,
            String name,
            String thumbnail,
            BigDecimal sellingPrice) {

        this.id = id;
        this.sku = sku;
        this.name = name;
        this.thumbnail = thumbnail;
        this.sellingPrice = sellingPrice;
    }

    public Long getId() {
        return id;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }
}