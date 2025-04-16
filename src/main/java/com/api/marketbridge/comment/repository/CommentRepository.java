package com.api.marketbridge.comment.repository;

import com.api.marketbridge.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByProductId(Long productId);
}
