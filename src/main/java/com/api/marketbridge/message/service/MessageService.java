package com.api.marketbridge.message.service;

import com.api.marketbridge.message.dto.ChatMessage;
import com.api.marketbridge.message.entity.Message;

import java.util.List;

public interface MessageService {
    Message sendMessage(ChatMessage chatMessage);
    List<Message> getConversation(String senderUsername, String receiverUsername, Long productId);
}
