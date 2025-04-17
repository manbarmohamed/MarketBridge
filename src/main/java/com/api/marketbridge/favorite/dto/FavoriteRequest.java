package com.api.marketbridge.favorite.dto;

import lombok.Data;

@Data
public class FavoriteRequest {
    private Long buyerId;
    private Long productId;
}
