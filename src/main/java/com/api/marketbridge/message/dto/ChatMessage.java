package com.api.marketbridge.message.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    private Long productId;
    private String content;
    private String senderUsername;
    private String receiverUsername;
}
