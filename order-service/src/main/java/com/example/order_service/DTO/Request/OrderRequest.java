package com.example.order_service.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/***
 * OrderRequest class is used to get the order request from the user
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {
    private List<OrderItemRequest> orderItems = new ArrayList<>();
    private String username;
    private String paymentMethod;
    private String shippingAddress;
    private String shippingMethod;
}
