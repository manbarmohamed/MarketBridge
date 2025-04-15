package com.api.marketbridge.comment.service;

import com.api.marketbridge.comment.dto.CommentRequest;
import com.api.marketbridge.comment.dto.CommentResponse;

import java.util.List;

public interface CommentService {
    CommentResponse addComment(CommentRequest request);
    CommentResponse updateComment(Long id, CommentRequest request);
    void deleteComment(Long id);
    List<CommentResponse> getCommentsByProduct(Long productId);
}
