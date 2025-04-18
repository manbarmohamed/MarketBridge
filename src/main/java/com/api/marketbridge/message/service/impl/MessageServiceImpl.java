package com.api.marketbridge.message.service.impl;

import com.api.marketbridge.message.dto.ChatMessage;
import com.api.marketbridge.message.entity.Message;
import com.api.marketbridge.message.repository.MessageRepository;
import com.api.marketbridge.message.service.MessageService;
import com.api.marketbridge.product.entity.Product;
import com.api.marketbridge.product.repository.ProductRepository;
import com.api.marketbridge.user.entity.User;
import com.api.marketbridge.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public Message sendMessage(ChatMessage chatMessage) {
        User sender = userRepository.findByUsername(chatMessage.getSenderUsername())
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findByUsername(chatMessage.getReceiverUsername())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));
        Product product = productRepository.findById(chatMessage.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(chatMessage.getContent());
        message.setSentAt(LocalDateTime.now());
        message.setProduct(product);

        return messageRepository.save(message);
    }

    @Override
    public List<Message> getConversation(String senderUsername, String receiverUsername, Long productId) {
        return messageRepository.findConversation(senderUsername, receiverUsername, productId);
    }
}
