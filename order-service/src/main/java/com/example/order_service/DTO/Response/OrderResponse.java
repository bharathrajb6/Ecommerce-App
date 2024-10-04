package com.example.order_service.DTO.Response;

import com.example.order_service.Model.OrderItems;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private List<OrderItemResponse> orderItems = new ArrayList<>();
    private double totalAmount;
    private String orderStatus;
    private String paymentStatus;
    private String paymentMethod;
    private String shippingAddress;
    private String shippingMethod;
    private String trackingNumber;
    private Timestamp updatedAt;
}
