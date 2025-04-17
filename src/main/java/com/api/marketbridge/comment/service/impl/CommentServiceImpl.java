package com.api.marketbridge.comment.service.impl;

import com.api.marketbridge.comment.dto.CommentRequest;
import com.api.marketbridge.comment.dto.CommentResponse;
import com.api.marketbridge.comment.entity.Comment;
import com.api.marketbridge.comment.mapper.CommentMapper;
import com.api.marketbridge.comment.repository.CommentRepository;
import com.api.marketbridge.comment.service.CommentService;
import com.api.marketbridge.product.entity.Product;
import com.api.marketbridge.product.repository.ProductRepository;
import com.api.marketbridge.user.entity.User;
import com.api.marketbridge.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

//    @Override
//    public CommentResponse addComment(CommentRequest request) {
//        // First convert the request to entity (this will have null relationships)
//        Comment comment = commentMapper.toEntity(request);
//
//        // Then manually set the relationships
//        Product product = productRepository.findById(request.getProductId())
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//        comment.setProduct(product);
//
//        User user = userRepository.findById(request.getUserId())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        comment.setAuthor(user);
//
//        // Set creation timestamp
//        comment.setCreatedAt(LocalDateTime.now());
//
//        // Save and return
//        Comment savedComment = commentRepository.save(comment);
//        return commentMapper.toResponse(savedComment);
//    }
@Override
public CommentResponse addComment(CommentRequest request) {
    Comment comment = commentMapper.toEntity(request);

    // Fetch product
    Product product = productRepository.findById(request.getProductId())
            .orElseThrow(() -> new RuntimeException("Product not found"));
    comment.setProduct(product);

    // ✅ Get currently authenticated user
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();

    User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    comment.setAuthor(user);

    comment.setCreatedAt(LocalDateTime.now());

    Comment savedComment = commentRepository.save(comment);
    return commentMapper.toResponse(savedComment);
}

    @Override
    public CommentResponse updateComment(Long id, CommentRequest request) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        commentMapper.partialUpdate(request, comment);
        Comment updatedComment = commentRepository.save(comment);
        return commentMapper.toResponse(updatedComment);
    }

    @Override
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        commentRepository.delete(comment);
    }

    @Override
    public List<CommentResponse> getCommentsByProduct(Long productId) {
        List<Comment> comments = commentRepository.findByProductId(productId);
       return comments.stream()
                .map(commentMapper::toResponse)
                .toList();
    }
}
