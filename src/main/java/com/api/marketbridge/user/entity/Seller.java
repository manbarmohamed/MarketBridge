package com.api.marketbridge.user.entity;

import com.api.marketbridge.product.entity.Product;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
public class Seller extends User {

    private Double rating;

    @OneToMany(mappedBy = "owner")
    private List<Product> products;
}