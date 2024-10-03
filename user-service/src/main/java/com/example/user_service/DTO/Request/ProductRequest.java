package com.example.user_service.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {
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
}
