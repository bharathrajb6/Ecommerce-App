package com.example.order_service.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItems {

    @Id
    @Column(name = "order_item_id")
    private String orderItemId;

    @Column(name = "product_id")
    private String productId;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price")
    private Double price;

    @Column(name = "total_price")
    private Double totalPrice;

    // Many-to-One relationship with Order
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;
}

