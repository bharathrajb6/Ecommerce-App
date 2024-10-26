package com.example.user_service.Service.Impl;

import com.example.user_service.DTO.Request.OrderRequest;
import com.example.user_service.DTO.Response.OrderResponse;
import com.example.user_service.Exceptions.OrderException;
import com.example.user_service.Service.OrderService;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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
        } catch (FeignException exception) {
            if (exception.status() == 404) {
                throw new OrderException("Resource not found");
            }
            if (exception.status() == 400) {
                throw new OrderException(exception.getMessage());
            }
            throw new OrderException("Bad request");
        }
    }

    public OrderResponse getOrderDetails(String orderID) {
        try {
            return orderService.getOrderDetails(orderID);
        } catch (FeignException exception) {
            if (exception.status() == 404) {
                throw new OrderException("Resource not found");
            }
            if (exception.status() == 400) {
                throw new OrderException(exception.getMessage());
            }
            throw new OrderException("Bad request");
        }
    }

    public List<OrderResponse> getAllOrderDetails() {
        try {
            return orderService.getAllOrders();
        } catch (FeignException exception) {
            if (exception.status() == 404) {
                throw new OrderException("Resource not found");
            }
            if (exception.status() == 400) {
                throw new OrderException(exception.getMessage());
            }
            throw new OrderException("Bad request");
        }
    }

    public OrderResponse updateOrderStatus(String orderID, String orderStatus) {
        try {
            return orderService.updateOrderStatus(orderID, orderStatus);
        } catch (FeignException exception) {
            if (exception.status() == 404) {
                throw new OrderException("Resource not found");
            }
            if (exception.status() == 400) {
                throw new OrderException(exception.getMessage());
            }
            throw new OrderException("Bad request");
        }
    }

    public String cancelOrder(String orderID) {
        try {
            return orderService.cancelOrder(orderID);
        } catch (FeignException exception) {
            if (exception.status() == 404) {
                throw new OrderException("Resource not found");
            }
            if (exception.status() == 400) {
                throw new OrderException(exception.getMessage());
            }
            throw new OrderException("Bad request");
        }
    }

    public List<OrderResponse> getAllCancelledOrders() {
        try {
            return orderService.getAllCancelledOrders(getUsername());
        } catch (FeignException exception) {
            if (exception.status() == 404) {
                throw new OrderException("Resource not found");
            }
            if (exception.status() == 400) {
                throw new OrderException(exception.getMessage());
            }
            throw new OrderException("Bad request");
        }
    }

    public OrderResponse getOrderByTrackingNumber(String trackingNumber) {
        try {
            return orderService.getOrderByTrackingNumber(trackingNumber);
        } catch (FeignException exception) {
            if (exception.status() == 404) {
                throw new OrderException("Resource not found");
            }
            if (exception.status() == 400) {
                throw new OrderException(exception.getMessage());
            }
            throw new OrderException("Bad request");
        }
    }

    public List<OrderResponse> getAllOrdersByUserName() {
        try {
            return orderService.getAllOrdersByUserName(getUsername());
        } catch (FeignException exception) {
            if (exception.status() == 404) {
                throw new OrderException("Resource not found");
            }
            if (exception.status() == 400) {
                throw new OrderException(exception.getMessage());
            }
            throw new OrderException("Bad request");
        }
    }
}
