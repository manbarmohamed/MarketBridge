package com.api.marketbridge.comment.dto;


import lombok.Data;

@Data
public class CommentRequest {
    private Long productId;
    private Long userId;
    private String content;
}
