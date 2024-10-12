package com.example.order_service.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/***
 * OrderItemResponse class is a response class for OrderItem
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemResponse {
    private String productId;
    private Integer quantity;
    private Double price;
    private Double totalPrice;
}
