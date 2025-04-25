package com.api.marketbridge.product.service.impl;


import com.api.marketbridge.category.entity.Category;
import com.api.marketbridge.category.repository.CategoryRepository;
import com.api.marketbridge.commun.ResourceNotFoundException;
import com.api.marketbridge.image.service.ImageService;
import com.api.marketbridge.product.dto.ProductRequest;
import com.api.marketbridge.product.dto.ProductResponse;
import com.api.marketbridge.product.entity.Product;
import com.api.marketbridge.product.entity.ProductImage;
import com.api.marketbridge.product.enums.ProductStatus;
import com.api.marketbridge.product.mapper.ProductMapper;
import com.api.marketbridge.product.repository.ProductRepository;

import com.api.marketbridge.product.service.ProductService;
import com.api.marketbridge.user.entity.Seller;
import com.api.marketbridge.user.repository.SellerRepository;
import io.jsonwebtoken.io.IOException;
import jakarta.transaction.Transactional;
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
    private final ImageService imageService;
    //private final ElasticProductRepository elasticRepo;

//    @PostConstruct
//    public void indexAllProducts() {
//        List<Product> products = productRepository.findAll();
//        List<ElasticProduct> elasticProducts = products.stream()
//                .map(this::toElasticProduct)
//                .toList();
//        elasticRepo.saveAll(elasticProducts);
//    }

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        Product product = productMapper.toEntity(request);
        Seller seller = sellerRepository.findById(request.getSellerId()).orElseThrow(() ->
                new ResourceNotFoundException("Seller not found"));
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() ->
                new ResourceNotFoundException("Category not found"));
        product.setCategory(category);
        product.setOwner(seller);
        product.setStatus(ProductStatus.PENDING);
        Product savedProduct = productRepository.save(product);

//        ElasticProduct elasticProduct = toElasticProduct(product);
//        elasticRepo.save(elasticProduct);

        return productMapper.toResponse(savedProduct);
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id).orElseThrow(() ->
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
        Product product = productRepository.findById(id).orElseThrow(() ->
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
        Page<Product> productPage = productRepository.findByOwnerId(sellerId, pageable);
        List<ProductResponse> productResponses = productPage.getContent()
                .stream()
                .map(productMapper::toResponse)
                .toList();
        return productResponses.isEmpty() ? Page.empty() : new PageImpl<>(productResponses, pageable, productPage.getTotalElements());

    }

    @Override
    public List<ProductResponse> searchProducts(String keyword) {
        List<Product> products = productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword);
        return products.stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Override
    public ProductResponse uploadImage(Long productId, String imageUrl) {
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new ResourceNotFoundException("Product not found"));
        if(product.getImageUrl() != null) {
            String publicId = extractPublicIdFromUrl(product.getImageUrl());
            try {
                imageService.deleteImage(publicId);
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete old image", e);
            }
        }
        product.setImageUrl(imageUrl);
        Product updatedProduct = productRepository.save(product);
        return productMapper.toResponse(updatedProduct);
    }

    @Override
    @Transactional
    public ProductResponse uploadMultipleImages(Long productId, List<String> imageUrls) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        List<ProductImage> productImages = imageUrls.stream()
                .map(url -> {
                    ProductImage image = new ProductImage();
                    image.setUrl(url);
                    image.setProduct(product);
                    return image;
                }).toList();

        product.getImages().addAll(productImages);
        productRepository.save(product); // this will cascade save images

        return productMapper.toResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse markProductAsSold(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new ResourceNotFoundException("Product not found"));
        product.setStatus(ProductStatus.SOLD);
        Product updatedProduct = productRepository.save(product);
        return productMapper.toResponse(updatedProduct);
    }

    @Override
    @Transactional
    public ProductResponse markProductAsAvailable(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new ResourceNotFoundException("Product not found"));
        product.setStatus(ProductStatus.AVAILABLE);
        return productMapper.toResponse(product);
    }

    @Override
    @Transactional
    public List<ProductResponse> getProductsByStatus(String status) {
        List<Product> products = productRepository.findByStatus(ProductStatus.valueOf(status));
        return products.stream()
                .map(productMapper::toResponse)
                .toList();
    }


//    public List<ProductResponse> elasticSearchProducts(String keyword) {
//        List<ElasticProduct> searchResults =
//                elasticRepo.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword);
//
//        List<Product> products = searchResults.stream()
//                .map(e -> productRepository.findById(Long.valueOf(e.getId())).orElse(null))
//                .filter(Objects::nonNull)
//                .toList();
//
//        return productMapper.toResponseList(products);
//    }
//
//    public ElasticProduct toElasticProduct(Product product) {
//        return ElasticProduct.builder()
//                .id(product.getId().toString())
//                .name(product.getName())
//                .description(product.getDescription())
//                .location(product.getLocation())
//                .price(product.getPrice())
//                .build();
//    }


    private String extractPublicIdFromUrl(String url) {
        if (url == null) return null;
        String[] parts = url.split("/");
        return parts[parts.length - 1].split("\\.")[0];
    }

}
