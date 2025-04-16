package com.api.marketbridge.user.entity;

import com.api.marketbridge.favorite.entity.Favorite;
import com.api.marketbridge.message.entity.Message;
import com.api.marketbridge.user.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity

public class Buyer extends User {

    @OneToMany(mappedBy = "sender")
    private List<Message> sentMessages;

    @OneToMany(mappedBy = "receiver")
    private List<Message> receivedMessages;

    @OneToMany(mappedBy = "user")
    private List<Favorite> favorites;

    public Buyer(){
        this.setRole(Role.BUYER);
    }
}