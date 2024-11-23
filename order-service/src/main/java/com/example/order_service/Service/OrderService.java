package com.example.order_service.Service;

import com.example.order_service.DTO.Request.OrderRequest;
import com.example.order_service.DTO.Response.OrderResponse;

import java.sql.Timestamp;
import java.util.List;

public interface OrderService {

    OrderResponse placeOrder(OrderRequest request);

    OrderResponse getOrderDetails(String orderID);

    List<OrderResponse> getAllOrders();

    OrderResponse updateOrderStatus(String orderID, String orderStatus);

    String cancelOrder(String orderID);

    List<OrderResponse> getAllCancelledOrdersForUser(String username);

    OrderResponse getOrderStatusByTrackNumber(String trackingNumber);

    List<OrderResponse> getAllOrdersByUserName(String username);

    List<OrderResponse> searchOrdersByCreatedDate(Timestamp criteria);

    int getTotalOrders();

    int getAllCancelledOrders();
}
