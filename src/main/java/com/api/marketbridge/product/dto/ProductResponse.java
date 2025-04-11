package com.api.marketbridge.product.dto;

import com.api.marketbridge.category.dto.CategoryResponse;
import com.api.marketbridge.product.enums.ProductStatus;
import lombok.Data;

@Data
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private ProductStatus status;
    private String createdAt;
    private String location;
    private CategoryResponse category;
    //private SellerResponse seller;
}
