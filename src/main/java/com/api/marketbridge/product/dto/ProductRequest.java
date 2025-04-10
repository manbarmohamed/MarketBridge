package com.api.marketbridge.product.dto;

import com.api.marketbridge.product.enums.ProductStatus;

public class ProductRequest {
    private String title;
    private String description;
    private double price;
    private ProductStatus status; // e.g., "AVAILABLE"
    private Long categoryId;
    private Long sellerId;
}
