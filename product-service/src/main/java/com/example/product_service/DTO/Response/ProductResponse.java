package com.example.product_service.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
    private String prodID;
    private String prodName;
    private String prodDescription;
    private String category;
    private int price;
    private int discountPrice;
    private int stock;
    private String currency;
    private String brand;
    private double weight;
    private String dimensions;
    private String color;
    private String size;
    private String material;
    private Timestamp created_at;
    private Timestamp updated_at;
}
