package com.api.marketbridge.favorite.controller;


import com.api.marketbridge.favorite.dto.FavoriteRequest;
import com.api.marketbridge.favorite.dto.FavoriteResponse;
import com.api.marketbridge.favorite.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping
    public ResponseEntity<FavoriteResponse> addToFavorites(FavoriteRequest request) {
        FavoriteResponse response = favoriteService.addToFavorites(request);
        return ResponseEntity.ok(response);
    }
}
