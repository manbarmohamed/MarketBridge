package com.api.marketbridge.auth.mapper;

import com.api.marketbridge.auth.dto.AuthDtos.SignupRequest;
import com.api.marketbridge.auth.dto.AuthDtos.AuthResponse;
import com.api.marketbridge.user.entity.Buyer;
import com.api.marketbridge.user.entity.Seller;
import com.api.marketbridge.user.entity.User;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class UserMapper {
    
    @Autowired
    protected PasswordEncoder passwordEncoder;
    
    // Convert SignupRequest to appropriate User type based on role
    public User toEntity(SignupRequest signupRequest) {
        if (signupRequest == null) {
            return null;
        }
        
        User user;
        
        if (signupRequest.getRole() == null || signupRequest.getRole() == com.api.marketbridge.user.enums.Role.BUYER) {
            user = new Buyer();
        } else {
            user = new Seller();
        }
        
        user.setFullName(signupRequest.getFullName());
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        // Encode password
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setPhone(signupRequest.getPhone());
        user.setAddress(signupRequest.getAddress());
        // Role is already set by constructor in Buyer/Seller
        
        return user;
    }
    
    @Mapping(target = "token", ignore = true)
    public abstract AuthResponse toResponse(User user);
}