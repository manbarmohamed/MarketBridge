package com.api.marketbridge;

import com.api.marketbridge.commun.ResourceNotFoundException;
import com.api.marketbridge.favorite.dto.FavoriteRequest;
import com.api.marketbridge.favorite.dto.FavoriteResponse;
import com.api.marketbridge.favorite.entity.Favorite;
import com.api.marketbridge.favorite.mapper.FavoriteMapper;
import com.api.marketbridge.favorite.repository.FavoriteRepository;
import com.api.marketbridge.favorite.service.impl.FavoriteServiceImpl;
import com.api.marketbridge.product.entity.Product;
import com.api.marketbridge.product.repository.ProductRepository;
import com.api.marketbridge.user.entity.Buyer;
import com.api.marketbridge.user.entity.User;
import com.api.marketbridge.user.repository.BuyerRepository;
import com.api.marketbridge.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceImplTest {

    @Mock
    private FavoriteRepository favoriteRepository; // example dependency

    @Mock
    private ProductRepository productRepository; // example dependency

    @InjectMocks
    private FavoriteServiceImpl favoriteService;

    private Product sampleProduct;
    private Favorite sampleFavorite;

    @BeforeEach
    void setUp() {
        // Initialize sample data objects used in tests
        sampleProduct = new Product();
        sampleProduct.setId(1L);
        sampleProduct.setName("Sample Product");
        // set other fields as needed

        sampleFavorite = new Favorite();
        sampleFavorite.setId(1L);
        sampleFavorite.setProduct(sampleProduct);
        // set other fields as needed
    }

}
