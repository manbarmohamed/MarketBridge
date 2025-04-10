package com.api.marketbridge.favorite.entity;

import com.api.marketbridge.product.entity.Product;
import com.api.marketbridge.user.entity.Buyer;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdAt;

    @ManyToOne
    private Buyer user;

    @ManyToOne
    private Product product;
}