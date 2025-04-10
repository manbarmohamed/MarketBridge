package com.api.marketbridge.product.dto;

import com.api.marketbridge.category.dto.CategoryResponse;
import com.api.marketbridge.product.enums.ProductStatus;

public class ProductResponse {
    private Long id;
    private String title;
    private String description;
    private Double price;
    private ProductStatus status;
    private String createdAt;
    private String updatedAt;

    private CategoryResponse category;
    //private SellerResponse seller;
}
