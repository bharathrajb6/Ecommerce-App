package com.example.analytics_reporting_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


/***
 * OrderResponse class is responsible for holding the response data of the order
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private String orderID;
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
