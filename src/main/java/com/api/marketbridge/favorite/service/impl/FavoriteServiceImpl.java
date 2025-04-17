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
import com.api.marketbridge.user.entity.User;
import com.api.marketbridge.user.repository.BuyerRepository;
import com.api.marketbridge.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final FavoriteMapper favoriteMapper;
    private final ProductRepository productRepository;
    private final BuyerRepository buyerRepository;
    private final UserRepository userRepository;

    @Override
    public FavoriteResponse addToFavorites(FavoriteRequest request) {
        // Step 1: Get authenticated username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // or email if that's your unique identifier

        // Step 2: Fetch the Buyer using the username
        Buyer buyer = buyerRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Authenticated buyer not found"));

        // Step 3: Fetch the product
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        // Step 4: Prevent users from favoriting their own products
        if (product.getOwner().getId().equals(buyer.getId())) {
            throw new IllegalArgumentException("You cannot favorite your own product");
        }

        // Step 5: Check for duplicates
        if (favoriteRepository.existsByUserIdAndProductId(buyer.getId(), product.getId())) {
            throw new IllegalArgumentException("Product already in favorites");
        }

        // Step 6: Create and save the Favorite
        Favorite favorite = new Favorite();
        favorite.setUser(buyer);
        favorite.setProduct(product);

        favorite = favoriteRepository.save(favorite);
        return favoriteMapper.toDto(favorite);
    }


    @Override
    public void removeFavorite(Long favoriteId) {
        Favorite favorite = favoriteRepository.findById(favoriteId).orElseThrow(
                ()-> new ResourceNotFoundException("Favorite not found")
        );
        favoriteRepository.delete(favorite);
    }

    @Override
    public List<FavoriteResponse> getFavoritesByBuyer(Long buyerId) {
        List<Favorite> favorites = favoriteRepository.findByUserId(buyerId);
        if (favorites.isEmpty()) {
            throw new ResourceNotFoundException("No favorites found for this buyer");
        }
        return favorites.stream()
                .map(favoriteMapper::toDto)
                .toList();
    }

    @Override
    public List<FavoriteResponse> getFavoritesForAuthenticatedBuyer() {
        // Get username from the SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Fetch user
        User buyer = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Authenticated user not found"));

        // Optional: Check if the user is a Buyer (based on your inheritance model)
        if (!(buyer instanceof Buyer)) {
            throw new AccessDeniedException("Only buyers can access this resource.");
        }

        List<Favorite> favorites = favoriteRepository.findByUserId(buyer.getId());
        if (favorites.isEmpty()) {
            throw new ResourceNotFoundException("No favorites found for this buyer");
        }

        return favorites.stream()
                .map(favoriteMapper::toDto)
                .toList();
    }
}
