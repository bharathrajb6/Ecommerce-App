package com.example.order_service.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/***
 * OrderItemRequest class is used to get the product id and quantity of the product
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemRequest {
    private String productId;
    private Integer quantity;
}
