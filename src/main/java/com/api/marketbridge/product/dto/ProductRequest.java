package com.api.marketbridge.product.dto;

import com.api.marketbridge.product.enums.ProductStatus;
import lombok.Data;

@Data
public class ProductRequest {
    private String name;
    private String description;
    private String location;
    private double price;
    private ProductStatus status; // e.g., "AVAILABLE"
    private Long categoryId;
    private Long sellerId;
}
