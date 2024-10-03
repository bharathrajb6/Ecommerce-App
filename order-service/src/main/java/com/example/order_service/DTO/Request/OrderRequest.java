package com.example.order_service.DTO.Request;

import com.example.order_service.Model.OrderItems;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {
    private List<OrderItems> orderItems = new ArrayList<>();
    private double totalAmount;
    private String orderStatus;
    private String paymentStatus;
    private String paymentMethod;
    private String shippingAddress;
    private String shippingMethod;
    private String trackingNumber;
}
