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
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

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
        Product product = productRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Product not found")
        );
        productMapper.updateProductFromRequest(request, product);
        Product productUpdated = productRepository.save(product);

        return productMapper.toResponse(productUpdated);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product not found"));
        productRepository.delete(product);
    }

    @Override
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Product not found"));
        return productMapper.toResponse(product);
    }

    @Override
    public Page<ProductResponse> getAllProducts(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> productPage = productRepository.findAll(pageable);
        List<ProductResponse> productResponses = productPage.getContent()
                .stream()
                .map(productMapper::toResponse)
                .toList();

        return productResponses.isEmpty() ? Page.empty() : new PageImpl<>(productResponses, pageable, productPage.getTotalElements());
    }

    @Override
    public Page<ProductResponse> getProductsByCategory(Long categoryId, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> productPage = productRepository.findByCategoryId(categoryId, pageable);
        List<ProductResponse> productResponses = productPage.getContent()
                .stream()
                .map(productMapper::toResponse)
                .toList();
        return productResponses.isEmpty() ? Page.empty() : new PageImpl<>(productResponses, pageable, productPage.getTotalElements());
    }

    @Override
    public Page<ProductResponse> getProductsBySeller(Long sellerId, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> productPage = productRepository.findBySellerId(sellerId, pageable);
        List<ProductResponse> productResponses = productPage.getContent()
                .stream()
                .map(productMapper::toResponse)
                .toList();
        return productResponses.isEmpty() ? Page.empty() : new PageImpl<>(productResponses, pageable, productPage.getTotalElements());

    }

    @Override
    public Page<ProductResponse> searchProducts(String keyword, Pageable pageable) {
        return null;
    }
}
