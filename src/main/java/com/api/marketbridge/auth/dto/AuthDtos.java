package com.api.marketbridge.auth.dto;

import com.api.marketbridge.user.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AuthDtos {
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignupRequest {
        private String fullName;
        private String username;
        private String email;
        private String password;
        private String phone;
        private String address;
        private Role role; // BUYER or SELLER
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginRequest {
        private String username;
        private String password;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthResponse {
        private Long id;
        private String fullName;
        private String username;
        private String email;
        private String token;
        private Role role;
    }
}