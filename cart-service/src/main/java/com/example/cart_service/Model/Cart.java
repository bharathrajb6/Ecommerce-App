package com.example.cart_service.Model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "cartItemsList")
public class Cart {
    @Id
    @Column(name = "cart_id")
    private String cartID;

    @Column(name = "user_id")
    private String username;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @OneToMany(mappedBy = "cart",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<CartItems> cartItemsList = new ArrayList<>();
}
