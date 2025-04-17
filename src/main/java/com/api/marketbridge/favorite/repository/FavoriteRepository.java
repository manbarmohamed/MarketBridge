package com.api.marketbridge.favorite.repository;

import com.api.marketbridge.favorite.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserId(Long buyerId);

    boolean existsByUserIdAndProductId(Long buyerId, Long productId);
}
