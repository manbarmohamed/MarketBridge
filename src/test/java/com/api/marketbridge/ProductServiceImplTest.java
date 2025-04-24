package com.api.marketbridge;
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
import com.api.marketbridge.product.service.impl.ProductServiceImpl;
import com.api.marketbridge.user.entity.Seller;
import com.api.marketbridge.user.repository.SellerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private ProductServiceImpl productService;

    private ProductRequest productRequest;
    private Product product;
    private ProductResponse productResponse;
    private Category category;
    private Seller seller;
    private List<Product> products;

    @BeforeEach
    void setUp() {
        // Initialize test data
        productRequest = new ProductRequest();
        productRequest.setSellerId(1L);
        productRequest.setCategoryId(1L);
        productRequest.setName("Test Product");
        productRequest.setDescription("Test Description");
        productRequest.setPrice(100.0);

        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(100.0);
        product.setStatus(ProductStatus.PENDING);
        product.setImages(new ArrayList<>());

        productResponse = new ProductResponse();
        productResponse.setId(1L);
        productResponse.setName("Test Product");
        productResponse.setDescription("Test Description");
        productResponse.setPrice(100.0);

        category = new Category();
        category.setId(1L);
        category.setName("Test Category");

        seller = new Seller();
        seller.setId(1L);
        seller.setFullName("Test Seller");

        products = List.of(product);
    }

    @Test
    @DisplayName("Should create a product successfully")
    void createProduct_Success() {
        // Arrange
        when(sellerRepository.findById(anyLong())).thenReturn(Optional.of(seller));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toEntity(any(ProductRequest.class))).thenReturn(product);
        when(productMapper.toResponse(any(Product.class))).thenReturn(productResponse);

        // Act
        ProductResponse result = productService.createProduct(productRequest);

        // Assert
        assertNotNull(result);
        assertEquals(productResponse.getId(), result.getId());
        assertEquals(productResponse.getName(), result.getName());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    @DisplayName("Should throw exception when seller not found")
    void createProduct_SellerNotFound() {
        // Arrange
        when(sellerRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> productService.createProduct(productRequest));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Should throw exception when category not found")
    void createProduct_CategoryNotFound() {
        // Arrange
        when(sellerRepository.findById(anyLong())).thenReturn(Optional.of(seller));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> productService.createProduct(productRequest));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Should update a product successfully")
    void updateProduct_Success() {
        // Arrange
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toResponse(any(Product.class))).thenReturn(productResponse);
        doNothing().when(productMapper).updateProductFromRequest(any(ProductRequest.class), any(Product.class));

        // Act
        ProductResponse result = productService.updateProduct(1L, productRequest);

        // Assert
        assertNotNull(result);
        assertEquals(productResponse.getId(), result.getId());
        verify(productRepository, times(1)).save(product);
        verify(productMapper, times(1)).updateProductFromRequest(productRequest, product);
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent product")
    void updateProduct_ProductNotFound() {
        // Arrange
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> productService.updateProduct(1L, productRequest));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Should delete a product successfully")
    void deleteProduct_Success() {
        // Arrange
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        doNothing().when(productRepository).delete(any(Product.class));

        // Act
        productService.deleteProduct(1L);

        // Assert
        verify(productRepository, times(1)).delete(product);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent product")
    void deleteProduct_ProductNotFound() {
        // Arrange
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> productService.deleteProduct(1L));
        verify(productRepository, never()).delete(any(Product.class));
    }

    @Test
    @DisplayName("Should get product by ID successfully")
    void getProductById_Success() {
        // Arrange
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(productMapper.toResponse(any(Product.class))).thenReturn(productResponse);

        // Act
        ProductResponse result = productService.getProductById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(productResponse.getId(), result.getId());
    }

    @Test
    @DisplayName("Should throw exception when getting non-existent product")
    void getProductById_ProductNotFound() {
        // Arrange
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(1L));
    }

    @Test
    @DisplayName("Should get all products with pagination")
    void getAllProducts_Success() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Product> productPage = new PageImpl<>(products, pageable, products.size());

        when(productRepository.findAll(any(Pageable.class))).thenReturn(productPage);
        when(productMapper.toResponse(any(Product.class))).thenReturn(productResponse);

        // Act
        Page<ProductResponse> result = productService.getAllProducts(0, 10, "id", "asc");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(productResponse.getId(), result.getContent().get(0).getId());
    }

    @Test
    @DisplayName("Should return empty page when no products exist")
    void getAllProducts_NoProducts() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Product> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(productRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

        // Act
        Page<ProductResponse> result = productService.getAllProducts(0, 10, "id", "asc");

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should get products by category")
    void getProductsByCategory_Success() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Product> productPage = new PageImpl<>(products, pageable, products.size());

        when(productRepository.findByCategoryId(anyLong(), any(Pageable.class))).thenReturn(productPage);
        when(productMapper.toResponse(any(Product.class))).thenReturn(productResponse);

        // Act
        Page<ProductResponse> result = productService.getProductsByCategory(1L, 0, 10, "id", "asc");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    @DisplayName("Should get products by seller")
    void getProductsBySeller_Success() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Product> productPage = new PageImpl<>(products, pageable, products.size());

        when(productRepository.findByOwnerId(anyLong(), any(Pageable.class))).thenReturn(productPage);
        when(productMapper.toResponse(any(Product.class))).thenReturn(productResponse);

        // Act
        Page<ProductResponse> result = productService.getProductsBySeller(1L, 0, 10, "id", "asc");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    @DisplayName("Should search products successfully")
    void searchProducts_Success() {
        // Arrange
        when(productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(anyString(), anyString()))
                .thenReturn(products);
        when(productMapper.toResponse(any(Product.class))).thenReturn(productResponse);

        // Act
        List<ProductResponse> result = productService.searchProducts("test");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should upload image successfully")
    void uploadImage_Success() {
        // Arrange
        product.setImageUrl("old-image-url.jpg");
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toResponse(any(Product.class))).thenReturn(productResponse);

        // Act
        ProductResponse result = productService.uploadImage(1L, "new-image-url.jpg");

        // Assert
        assertNotNull(result);
        assertEquals("new-image-url.jpg", product.getImageUrl());
    }

    @Test
    @DisplayName("Should upload multiple images successfully")
    void uploadMultipleImages_Success() {
        // Arrange
        List<String> imageUrls = List.of("image1.jpg", "image2.jpg");
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toResponse(any(Product.class))).thenReturn(productResponse);

        // Act
        ProductResponse result = productService.uploadMultipleImages(1L, imageUrls);

        // Assert
        assertNotNull(result);
        assertEquals(2, product.getImages().size());
    }

    @Test
    @DisplayName("Should mark product as sold")
    void markProductAsSold_Success() {
        // Arrange
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toResponse(any(Product.class))).thenReturn(productResponse);

        // Act
        ProductResponse result = productService.markProductAsSold(1L);

        // Assert
        assertNotNull(result);
        assertEquals(ProductStatus.SOLD, product.getStatus());
    }

    @Test
    @DisplayName("Should mark product as available")
    void markProductAsAvailable_Success() {
        // Arrange
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toResponse(any(Product.class))).thenReturn(productResponse);

        // Act
        ProductResponse result = productService.markProductAsAvailable(1L);

        // Assert
        assertNotNull(result);
        assertEquals(ProductStatus.AVAILABLE, product.getStatus());
    }
}
