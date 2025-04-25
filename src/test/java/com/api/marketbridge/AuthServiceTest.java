package com.api.marketbridge;

import com.api.marketbridge.auth.dto.AuthDtos.LoginRequest;
import com.api.marketbridge.auth.dto.AuthDtos.SignupRequest;
import com.api.marketbridge.auth.dto.AuthDtos.AuthResponse;
import com.api.marketbridge.auth.mapper.UserMapper;
import com.api.marketbridge.auth.service.AuthService;
import com.api.marketbridge.config.JwtUtils;
import com.api.marketbridge.user.entity.Admin;
import com.api.marketbridge.user.entity.User;
import com.api.marketbridge.user.enums.Role;
import com.api.marketbridge.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthService authService;

    private SignupRequest signupRequest;
    private LoginRequest loginRequest;
    private User user;
    private AuthResponse authResponse;
    private String token;

    @BeforeEach
    void setUp() {
        // Setup test data
        signupRequest = new SignupRequest();
        signupRequest.setUsername("testuser");
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("password123");
        signupRequest.setFullName("Test User");

        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");

        // Mock the abstract User class
        user = mock(User.class);

        // Use lenient stubs to avoid unnecessary stubbing exceptions
        lenient().when(user.getId()).thenReturn(1L);
        lenient().when(user.getUsername()).thenReturn("testuser");
        lenient().when(user.getEmail()).thenReturn("test@example.com");
        lenient().when(user.getPassword()).thenReturn("encodedPassword");
        lenient().when(user.getRole()).thenReturn(Role.BUYER);

        authResponse = new AuthResponse();
        authResponse.setId(1L);
        authResponse.setUsername("testuser");
        authResponse.setEmail("test@example.com");
        authResponse.setRole(Role.BUYER);

        token = "jwt-token";
    }

    @Test
    void testInit_WhenNoAdminExists_ShouldCreateAdmin() {
        // Arrange
        when(userRepository.findByRole(Role.ADMIN)).thenReturn(new ArrayList<>());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(Admin.class))).thenAnswer(invocation -> {
            Admin admin = invocation.getArgument(0);
            admin.setId(1L);
            return admin;
        });

        // Act
        authService.init();

        // Assert
        verify(userRepository).findByRole(Role.ADMIN);
        verify(passwordEncoder).encode("admin123");
        verify(userRepository).save(any(Admin.class));
    }

    @Test
    void testInit_WhenAdminExists_ShouldNotCreateAdmin() {
        // Arrange
        List<User> admins = new ArrayList<>();
        admins.add(new Admin());
        when(userRepository.findByRole(Role.ADMIN)).thenReturn(admins);

        // Act
        authService.init();

        // Assert
        verify(userRepository).findByRole(Role.ADMIN);
        verifyNoMoreInteractions(passwordEncoder);
        verify(userRepository, never()).save(any(Admin.class));
    }

    @Test
    void testSignup_WhenUsernameExists_ShouldThrowException() {
        // Arrange
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.signup(signupRequest);
        });
        assertEquals("Username already exists", exception.getMessage());
        verify(userRepository).existsByUsername("testuser");
        verifyNoMoreInteractions(userRepository);
    }

}