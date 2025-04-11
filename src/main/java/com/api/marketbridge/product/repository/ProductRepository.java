package com.api.marketbridge.product.repository;

import com.api.marketbridge.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
