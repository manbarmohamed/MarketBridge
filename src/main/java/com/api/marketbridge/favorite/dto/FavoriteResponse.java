package com.api.marketbridge.favorite.dto;

import com.api.marketbridge.product.dto.ProductResponse;
import lombok.Data;


@Data
public class FavoriteResponse {
    private Long id;
    private Long buyerId;
    private ProductResponse product;
}
