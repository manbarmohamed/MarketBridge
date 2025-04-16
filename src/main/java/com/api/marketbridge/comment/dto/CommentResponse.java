package com.api.marketbridge.comment.dto;

import lombok.Data;

@Data
public class CommentResponse {
    private Long id;
    private String content;
    private String timestamp;

    private String productName;

    private String authorName;
}
