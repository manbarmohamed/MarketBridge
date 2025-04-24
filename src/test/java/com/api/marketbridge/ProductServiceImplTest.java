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
}
