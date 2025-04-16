package com.api.marketbridge.comment.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponse {
    Long id;
    String content;
    LocalDateTime createdAt;
    String authorFullName;
    String productName;
}
