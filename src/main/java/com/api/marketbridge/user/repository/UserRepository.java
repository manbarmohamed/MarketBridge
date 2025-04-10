package com.api.marketbridge.user.repository;

import com.api.marketbridge.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);
    User findByUsername(String username);
    User findUserByUsernameOrEmail(String username, String email);
}
