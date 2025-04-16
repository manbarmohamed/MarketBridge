package com.api.marketbridge.comment.controller;


import com.api.marketbridge.comment.dto.CommentRequest;
import com.api.marketbridge.comment.dto.CommentResponse;
import com.api.marketbridge.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponse> addComment(CommentRequest request) {
        CommentResponse response = commentService.addComment(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable("id") Long id, CommentRequest request) {
        CommentResponse response = commentService.updateComment(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable("id") Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<CommentResponse>> getCommentsByProduct(@PathVariable("productId") Long productId) {
        List<CommentResponse> responses = commentService.getCommentsByProduct(productId);
        return ResponseEntity.ok(responses);
    }
}
