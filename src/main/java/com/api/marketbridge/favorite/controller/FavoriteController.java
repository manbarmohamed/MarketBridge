package com.api.marketbridge.favorite.controller;


import com.api.marketbridge.favorite.dto.FavoriteRequest;
import com.api.marketbridge.favorite.dto.FavoriteResponse;
import com.api.marketbridge.favorite.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeFromFavorites(@PathVariable("id") Long id) {
        favoriteService.removeFavorite(id);
        return ResponseEntity.noContent().build();
    }
}
