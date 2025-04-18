package com.api.marketbridge.message.controller;

import com.api.marketbridge.message.dto.ChatMessage;
import com.api.marketbridge.message.entity.Message;
import com.api.marketbridge.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;

    @MessageMapping("/chat")
    public void handleMessage(@Payload ChatMessage message) {
        // Save to database
        Message savedMessage = messageService.sendMessage(message);

        // Send real-time message to receiver
        messagingTemplate.convertAndSend(
            "/topic/messages/" + message.getReceiverUsername(),
            message
        );
    }
}
