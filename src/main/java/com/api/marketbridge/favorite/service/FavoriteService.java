package com.api.marketbridge.favorite.service;

import com.api.marketbridge.favorite.dto.FavoriteRequest;
import com.api.marketbridge.favorite.dto.FavoriteResponse;

import java.util.List;

public interface FavoriteService {
    FavoriteResponse addToFavorites(FavoriteRequest request);
    void removeFavorite(Long favoriteId);
    List<FavoriteResponse> getFavoritesByBuyer(Long buyerId);
}
