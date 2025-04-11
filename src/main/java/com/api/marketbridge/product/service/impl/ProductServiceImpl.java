package com.api.marketbridge.product.service.impl;


import com.api.marketbridge.category.entity.Category;
import com.api.marketbridge.category.repository.CategoryRepository;
import com.api.marketbridge.commun.ResourceNotFoundException;
import com.api.marketbridge.product.dto.ProductRequest;
import com.api.marketbridge.product.dto.ProductResponse;
import com.api.marketbridge.product.entity.Product;
import com.api.marketbridge.product.enums.ProductStatus;
import com.api.marketbridge.product.mapper.ProductMapper;
import com.api.marketbridge.product.repository.ProductRepository;
import com.api.marketbridge.product.service.ProductService;
import com.api.marketbridge.user.entity.Seller;
import com.api.marketbridge.user.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    private final SellerRepository sellerRepository;

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        Product product = productMapper.toEntity(request);
        Seller seller = sellerRepository.findById(request.getSellerId()).orElseThrow(()->
                new ResourceNotFoundException("Seller not found"));
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(()->
                new ResourceNotFoundException("Category not found"));
        product.setCategory(category);
        product.setOwner(seller);
        product.setStatus(ProductStatus.PENDING);
        Product savedProduct = productRepository.save(product);
        return productMapper.toResponse(savedProduct);
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        return null;
    }

    @Override
    public void deleteProduct(Long id) {

    }

    @Override
    public ProductResponse getProductById(Long id) {
        return null;
    }

    @Override
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return null;
    }

    @Override
    public Page<ProductResponse> getProductsByCategory(Long categoryId, Pageable pageable) {
        return null;
    }

    @Override
    public Page<ProductResponse> getProductsBySeller(Long sellerId, Pageable pageable) {
        return null;
    }

    @Override
    public Page<ProductResponse> searchProducts(String keyword, Pageable pageable) {
        return null;
    }
}
