package com.example.user_service.Service.Impl;

import com.example.user_service.DTO.Request.OrderRequest;
import com.example.user_service.DTO.Response.OrderResponse;
import com.example.user_service.Exceptions.OrderException;
import com.example.user_service.Exceptions.ProductException;
import com.example.user_service.Service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class OrderServiceImpl {
    @Autowired
    private OrderService orderService;

    private String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public OrderResponse placeOrder(OrderRequest request) {
        request.setUsername(getUsername());
        try {
            return orderService.placeOrder(request);
        } catch (ProductException productException) {
            log.error(productException.getMessage());
            throw new ProductException(productException.getMessage());
        } catch (OrderException orderException) {
            log.error(orderException.getMessage());
            throw new OrderException(orderException.getMessage());
        }
    }

    public OrderResponse getOrderDetails(String orderID) {
        try {
            return orderService.getOrderDetails(orderID);
        } catch (OrderException orderException) {
            log.error(orderException.getMessage());
            throw new OrderException(orderException.getMessage());
        }
    }

    public List<OrderResponse> getAllOrderDetails() {
        try {
            return orderService.getAllOrders();
        } catch (OrderException orderException) {
            log.error(orderException.getMessage());
            throw new OrderException(orderException.getMessage());
        }
    }

    public OrderResponse updateOrderStatus(String orderID, String orderStatus) {
        try {
            return orderService.updateOrderStatus(orderID, orderStatus);
        } catch (OrderException orderException) {
            log.error(orderException.getMessage());
            throw new OrderException(orderException.getMessage());
        }
    }

    public String cancelOrder(String orderID) {
        try {
            return orderService.cancelOrder(orderID);
        } catch (OrderException orderException) {
            log.error(orderException.getMessage());
            throw new OrderException(orderException.getMessage());
        }
    }

    public List<OrderResponse> getAllCancelledOrders() {
        try {
            return orderService.getAllCancelledOrders(getUsername());
        } catch (OrderException orderException) {
            log.error(orderException.getMessage());
            throw new OrderException(orderException.getMessage());
        }
    }

    public OrderResponse getOrderByTrackingNumber(String trackingNumber) {
        try {
            return orderService.getOrderByTrackingNumber(trackingNumber);
        } catch (OrderException orderException) {
            log.error(orderException.getMessage());
            throw new OrderException(orderException.getMessage());
        }
    }

    public List<OrderResponse> getAllOrdersByUserName() {
        try {
            return orderService.getAllOrdersByUserName(getUsername());
        } catch (OrderException orderException) {
            log.error(orderException.getMessage());
            throw new OrderException(orderException.getMessage());
        }
    }
}
