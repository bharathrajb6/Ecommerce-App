package com.example.product_service.DTO.Request.Product;

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
    private ProductCategoryRequest category;
    private ProductBrandRequest brand;
    private int price;
    private int discountPrice;
    private int stock;
    private String currency;
    private double weight;
    private String dimensions;
    private String color;
    private String size;
    private String material;
}
