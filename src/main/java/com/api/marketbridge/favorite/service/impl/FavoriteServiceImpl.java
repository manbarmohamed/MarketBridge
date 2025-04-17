package com.api.marketbridge.favorite.service.impl;

import com.api.marketbridge.commun.ResourceNotFoundException;
import com.api.marketbridge.favorite.dto.FavoriteRequest;
import com.api.marketbridge.favorite.dto.FavoriteResponse;
import com.api.marketbridge.favorite.entity.Favorite;
import com.api.marketbridge.favorite.mapper.FavoriteMapper;
import com.api.marketbridge.favorite.repository.FavoriteRepository;
import com.api.marketbridge.favorite.service.FavoriteService;
import com.api.marketbridge.product.entity.Product;
import com.api.marketbridge.product.repository.ProductRepository;
import com.api.marketbridge.user.entity.Buyer;
import com.api.marketbridge.user.repository.BuyerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final FavoriteMapper favoriteMapper;
    private final ProductRepository productRepository;
    private final BuyerRepository buyerRepository;

    @Override
    public FavoriteResponse addToFavorites(FavoriteRequest request) {
        Product product = productRepository.findById(request.getProductId()).orElseThrow(
                () -> new ResourceNotFoundException("Product not found")
        );
        if (product.getOwner().getId().equals(request.getBuyerId())) {
            throw new IllegalArgumentException("You cannot favorite your own product");
        }
        if (favoriteRepository.existsByUserIdAndProductId(request.getBuyerId(), request.getProductId())) {
            throw new IllegalArgumentException("Product already in favorites");
        }
        Buyer buyer = buyerRepository.findById(request.getBuyerId()).orElseThrow(
                () -> new ResourceNotFoundException("Buyer not found")
        );
        Favorite favorite = favoriteMapper.toEntity(request);
        favorite.setProduct(product);
        favorite.setUser(buyer);
        favorite = favoriteRepository.save(favorite);
        return favoriteMapper.toDto(favorite);
    }

    @Override
    public void removeFavorite(Long favoriteId) {

    }

    @Override
    public List<FavoriteResponse> getFavoritesByBuyer(Long buyerId) {
        return List.of();
    }
}
