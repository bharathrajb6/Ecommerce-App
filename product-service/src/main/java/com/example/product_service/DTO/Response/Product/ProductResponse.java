package com.example.product_service.DTO.Response.Product;

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
    private ProductCategoryResponse category;
    private ProductBrandResponse brand;
    private int price;
    private int discountPrice;
    private int stock;
    private String currency;
    private double weight;
    private String dimensions;
    private String color;
    private String size;
    private String material;
    private Timestamp created_at;
    private Timestamp updated_at;
}
