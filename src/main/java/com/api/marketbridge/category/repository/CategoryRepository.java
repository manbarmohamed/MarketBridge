package com.api.marketbridge.category.repository;

import com.api.marketbridge.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
