package com.api.marketbridge.favorite.service.impl;

import com.api.marketbridge.favorite.dto.FavoriteRequest;
import com.api.marketbridge.favorite.dto.FavoriteResponse;
import com.api.marketbridge.favorite.service.FavoriteService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteService {
    @Override
    public FavoriteResponse addToFavorites(FavoriteRequest request) {
        return null;
    }

    @Override
    public void removeFavorite(Long favoriteId) {

    }

    @Override
    public List<FavoriteResponse> getFavoritesByBuyer(Long buyerId) {
        return List.of();
    }
}
