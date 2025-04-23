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
import com.api.marketbridge.user.entity.Seller;
import com.api.marketbridge.user.repository.BuyerRepository;
import com.api.marketbridge.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceImplTest {

    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private FavoriteMapper favoriteMapper;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private BuyerRepository buyerRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FavoriteServiceImpl favoriteService;

    private FavoriteRequest favoriteRequest;
    private Product sampleProduct;
    private Buyer sampleBuyer;
    private Seller sampleSeller;
    private Favorite sampleFavorite;

    @BeforeEach
    void setUp() {
        // Initialize sample data
        favoriteRequest = new FavoriteRequest();
        favoriteRequest.setProductId(1L);

        sampleSeller = new Seller();
        sampleSeller.setId(2L);

        sampleProduct = new Product();
        sampleProduct.setId(1L);
        sampleProduct.setName("Sample Product");
        sampleProduct.setOwner(sampleSeller);

        sampleBuyer = new Buyer();
        sampleBuyer.setId(3L);
        sampleBuyer.setUsername("testuser");

        sampleFavorite = new Favorite();
        sampleFavorite.setId(10L);
        sampleFavorite.setUser(sampleBuyer);
        sampleFavorite.setProduct(sampleProduct);
    }

    @Test
    void addToFavorites_Success() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getName()).thenReturn("testuser");
        when(buyerRepository.findByUsername("testuser")).thenReturn(Optional.of(sampleBuyer));
        when(productRepository.findById(favoriteRequest.getProductId())).thenReturn(Optional.of(sampleProduct));
        when(favoriteRepository.existsByUserIdAndProductId(sampleBuyer.getId(), sampleProduct.getId())).thenReturn(false);
        when(favoriteRepository.save(any(Favorite.class))).thenReturn(sampleFavorite);
        when(favoriteMapper.toDto(sampleFavorite)).thenReturn(new FavoriteResponse());

        // Act
        FavoriteResponse response = favoriteService.addToFavorites(favoriteRequest);

        // Assert
        assertNotNull(response);
        verify(favoriteRepository).save(any(Favorite.class));
    }

    @Test
    void addToFavorites_BuyerNotFound() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getName()).thenReturn("testuser");
        when(buyerRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> favoriteService.addToFavorites(favoriteRequest));
        verify(favoriteRepository, never()).save(any(Favorite.class));
    }

    @Test
    void addToFavorites_ProductNotFound() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getName()).thenReturn("testuser");
        when(buyerRepository.findByUsername("testuser")).thenReturn(Optional.of(sampleBuyer));
        when(productRepository.findById(favoriteRequest.getProductId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> favoriteService.addToFavorites(favoriteRequest));
        verify(favoriteRepository, never()).save(any(Favorite.class));
    }

    @Test
    void addToFavorites_OwnProduct() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getName()).thenReturn("testuser");
        when(buyerRepository.findByUsername("testuser")).thenReturn(Optional.of(sampleBuyer));
        when(productRepository.findById(favoriteRequest.getProductId())).thenReturn(Optional.of(sampleProduct));

        // Set the product owner to the same ID as the buyer to simulate owning the product
        sampleSeller.setId(sampleBuyer.getId());
        when(productRepository.findById(favoriteRequest.getProductId())).thenReturn(Optional.of(sampleProduct));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> favoriteService.addToFavorites(favoriteRequest));
        verify(favoriteRepository, never()).save(any(Favorite.class));
    }

    @Test
    void addToFavorites_DuplicateFavorite() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getName()).thenReturn("testuser");
        when(buyerRepository.findByUsername("testuser")).thenReturn(Optional.of(sampleBuyer));
        when(productRepository.findById(favoriteRequest.getProductId())).thenReturn(Optional.of(sampleProduct));
        when(favoriteRepository.existsByUserIdAndProductId(sampleBuyer.getId(), sampleProduct.getId())).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> favoriteService.addToFavorites(favoriteRequest));
        verify(favoriteRepository, never()).save(any(Favorite.class));
    }

    @Test
    void removeFavorite_Success() {
        // Arrange
        Long favoriteId = 1L;
        when(favoriteRepository.findById(favoriteId)).thenReturn(Optional.of(sampleFavorite));

        // Act
        favoriteService.removeFavorite(favoriteId);

        // Assert
        verify(favoriteRepository).delete(sampleFavorite);
    }

    @Test
    void removeFavorite_NotFound() {
        // Arrange
        Long favoriteId = 1L;
        when(favoriteRepository.findById(favoriteId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> favoriteService.removeFavorite(favoriteId));
        verify(favoriteRepository, never()).delete(any(Favorite.class));
    }

    @Test
    void getFavoritesByBuyer_Success() {
        //Arrange
        Long buyerId = 1L;
        when(favoriteRepository.findByUserId(buyerId)).thenReturn(java.util.List.of(sampleFavorite));
        when(favoriteMapper.toDto(sampleFavorite)).thenReturn(new FavoriteResponse());

        //Act
        favoriteService.getFavoritesByBuyer(buyerId);

        //Assert
        verify(favoriteRepository).findByUserId(buyerId);
        verify(favoriteMapper, times(1)).toDto(sampleFavorite);
    }

    @Test
    void getFavoritesByBuyer_EmptyList() {
        //Arrange
        Long buyerId = 1L;
        when(favoriteRepository.findByUserId(buyerId)).thenReturn(java.util.List.of());

        //Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> favoriteService.getFavoritesByBuyer(buyerId));
    }

    @Test
    void getFavoritesForAuthenticatedBuyer_Success() {
        //Arrange
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getName()).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(sampleBuyer));
        when(favoriteRepository.findByUserId(sampleBuyer.getId())).thenReturn(java.util.List.of(sampleFavorite));
        when(favoriteMapper.toDto(sampleFavorite)).thenReturn(new FavoriteResponse());

        //Act
        favoriteService.getFavoritesForAuthenticatedBuyer();

        //Assert
        verify(favoriteRepository).findByUserId(sampleBuyer.getId());
        verify(favoriteMapper, times(1)).toDto(sampleFavorite);
    }

    @Test
    void getFavoritesForAuthenticatedBuyer_UserNotFound() {
        //Arrange
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getName()).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        //Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> favoriteService.getFavoritesForAuthenticatedBuyer());
    }

    @Test
    void getFavoritesForAuthenticatedBuyer_NotBuyer() {
        //Arrange
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getName()).thenReturn("testuser");

        Seller sampleSeller = new Seller();
        sampleSeller.setId(2L);
        sampleSeller.setUsername("testuser");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(sampleSeller));

        //Act & Assert
        assertThrows(AccessDeniedException.class, () -> favoriteService.getFavoritesForAuthenticatedBuyer());
    }

    @Test
    void getFavoritesForAuthenticatedBuyer_NoFavorites() {
        //Arrange
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getName()).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(sampleBuyer));
        when(favoriteRepository.findByUserId(sampleBuyer.getId())).thenReturn(java.util.List.of());

        //Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> favoriteService.getFavoritesForAuthenticatedBuyer());
    }
}
