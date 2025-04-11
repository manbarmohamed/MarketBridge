package com.api.marketbridge.user.repository;

import com.api.marketbridge.user.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Long> {
}
