package com.cartit.dto.response.common;

import java.math.BigDecimal;
import com.cartit.enums.Unit;

public class ProductSummaryResponse {

    private Long id;
    private String sku;
    private String name;
    private String thumbnail;
    private BigDecimal mrp;
    private BigDecimal sellingPrice;
    private Unit unit;
    private BigDecimal unitQuantity;

    public ProductSummaryResponse() {
    }

    public ProductSummaryResponse(
            Long id,
            String sku,
            String name,
            String thumbnail,
            BigDecimal mrp,
            BigDecimal sellingPrice,
            Unit unit,
            BigDecimal unitQuantity) {

        this.id = id;
        this.sku = sku;
        this.name = name;
        this.thumbnail = thumbnail;
        this.mrp = mrp;
        this.sellingPrice = sellingPrice;
        this.unit = unit;
        this.unitQuantity = unitQuantity;
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

    public BigDecimal getMrp() {
        return mrp;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public Unit getUnit() {
        return unit;
    }

    public BigDecimal getUnitQuantity() {
        return unitQuantity;
    }
}