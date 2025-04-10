package com.api.marketbridge.user.entity;

import com.api.marketbridge.favorite.entity.Favorite;
import com.api.marketbridge.message.entity.Message;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Buyer extends User {

    @OneToMany(mappedBy = "sender")
    private List<Message> sentMessages;

    @OneToMany(mappedBy = "receiver")
    private List<Message> receivedMessages;

    @OneToMany(mappedBy = "user")
    private List<Favorite> favorites;
}