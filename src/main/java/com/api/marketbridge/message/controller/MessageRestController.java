package com.api.marketbridge.message.controller;

import com.api.marketbridge.message.entity.Message;
import com.api.marketbridge.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageRestController {

    private final MessageService messageService;

    @GetMapping("/conversation")
    public ResponseEntity<List<Message>> getConversation(
            @RequestParam String sender,
            @RequestParam String receiver,
            @RequestParam Long productId
    ) {
        return ResponseEntity.ok(messageService.getConversation(sender, receiver, productId));
    }
}
