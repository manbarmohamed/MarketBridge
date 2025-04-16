package com.api.marketbridge.comment.entity;

import com.api.marketbridge.product.entity.Product;
import com.api.marketbridge.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO for {@link com.api.marketbridge.comment.entity.CommentEntity}
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private LocalDateTime createdAt;

    @ManyToOne
    private User author;

    @ManyToOne
    private Product product;
}