package com.api.marketbridge.product.service;

import com.api.marketbridge.product.dto.ProductRequest;
import com.api.marketbridge.product.dto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request);
    ProductResponse updateProduct(Long id, ProductRequest request);
    void deleteProduct(Long id);
    ProductResponse getProductById(Long id);

    Page<ProductResponse> getAllProducts(int page, int size, String sortBy, String sortDir);
    Page<ProductResponse> getProductsByCategory(Long categoryId, int page, int size, String sortBy, String sortDir);
    Page<ProductResponse> getProductsBySeller(Long sellerId, int page, int size, String sortBy, String sortDir);
    List<ProductResponse> searchProducts(String keyword);
}
