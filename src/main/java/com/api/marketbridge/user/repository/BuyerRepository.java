package com.api.marketbridge.user.repository;

import com.api.marketbridge.user.entity.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuyerRepository extends JpaRepository<Buyer, Long> {
}
