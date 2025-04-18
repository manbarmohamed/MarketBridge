package com.api.marketbridge.message.repository;

import com.api.marketbridge.message.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE " +
            "((m.sender.username = :sender AND m.receiver.username = :receiver) " +
            "OR (m.sender.username = :receiver AND m.receiver.username = :sender)) " +
            "AND m.product.id = :productId ORDER BY m.sentAt")
    List<Message> findConversation(@Param("sender") String sender,
                                   @Param("receiver") String receiver,
                                   @Param("productId") Long productId);
}

