package com.example.product_service.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @Column(name = "prod_id")
    private String prodID;

    @Column(name = "prod_name")
    private String prodName;

    @Column(name = "prod_description")
    private String prodDescription;

    @Column(name = "category")
    private String category;

    @Column(name = "price")
    private int price;

    @Column(name = "discount_price")
    private int discountPrice;

    @Column(name = "stock")
    private int stock;

    @Column(name = "currency")
    private String currency;

    @Column(name = "brand")
    private String brand;

    @Column(name = "weight")
    private double weight;

    @Column(name = "dimensions")
    private String dimensions;

    @Column(name = "color")
    private String color;

    @Column(name = "size")
    private String size;

    @Column(name = "material")
    private String material;

    @Column(name = "created_at")
    private Timestamp created_at;

    @Column(name = "updated_at")
    private Timestamp updated_at;
}
