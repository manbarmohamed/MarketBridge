package com.api.marketbridge.comment.service.impl;

import com.api.marketbridge.comment.dto.CommentRequest;
import com.api.marketbridge.comment.dto.CommentResponse;
import com.api.marketbridge.comment.entity.Comment;
import com.api.marketbridge.comment.mapper.CommentMapper;
import com.api.marketbridge.comment.repository.CommentRepository;
import com.api.marketbridge.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Override
    public CommentResponse addComment(CommentRequest request) {
        Comment comment = commentMapper.toEntity(request);
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

    }

    @Override
    public List<CommentResponse> getCommentsByProduct(Long productId) {
        return List.of();
    }
}
