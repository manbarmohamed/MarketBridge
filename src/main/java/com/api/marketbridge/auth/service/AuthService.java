package com.api.marketbridge.auth.service;

import com.api.marketbridge.auth.dto.AuthDtos.LoginRequest;
import com.api.marketbridge.auth.dto.AuthDtos.SignupRequest;
import com.api.marketbridge.auth.dto.AuthDtos.AuthResponse;
import com.api.marketbridge.auth.mapper.UserMapper;
import com.api.marketbridge.config.JwtUtils;
import com.api.marketbridge.user.entity.Admin;
import com.api.marketbridge.user.entity.User;
import com.api.marketbridge.user.enums.Role;
import com.api.marketbridge.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        System.out.println("Starting admin initialization...");

        List<User> admins = userRepository.findByRole(Role.ADMIN);
        System.out.println("Found " + admins.size() + " existing admins in database");

        if (admins.isEmpty()) {
            System.out.println("No admin found, creating default admin user");
            try {
                Admin admin = new Admin();
                admin.setUsername("admin");
                admin.setEmail("admin@marketbridge.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setFullName("System Admin");
                admin.setRole(Role.ADMIN);

                User savedAdmin = userRepository.save(admin);
                System.out.println("Admin user created successfully with ID: " + savedAdmin.getId());
            } catch (Exception e) {
                System.err.println("Failed to create admin user: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Admin user already exists, skipping creation");
        }
    }
    
    public AuthResponse signup(SignupRequest request) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        // Convert DTO to entity
        User user = userMapper.toEntity(request);
        
        // Save user
        User savedUser = userRepository.save(user);
        
        // Generate JWT token
        String token = jwtUtils.generateToken(savedUser, savedUser.getRole());
        
        // Map to response DTO
        AuthResponse response = userMapper.toResponse(savedUser);
        response.setToken(token);
        
        return response;
    }
    
    public AuthResponse login(LoginRequest request) {
        // Authenticate the user
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        
        // If authentication successful, get user details
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Generate JWT token
        String token = jwtUtils.generateToken(user, user.getRole());
        
        // Map to response DTO
        AuthResponse response = userMapper.toResponse(user);
        response.setToken(token);
        
        return response;
    }
}