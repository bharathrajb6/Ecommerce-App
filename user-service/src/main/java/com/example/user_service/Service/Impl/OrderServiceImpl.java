package com.example.user_service.Service.Impl;

import com.example.user_service.DTO.Request.OrderRequest;
import com.example.user_service.DTO.Response.OrderResponse;
import com.example.user_service.Exceptions.OrderException;
import com.example.user_service.Exceptions.ProductException;
import com.example.user_service.Service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class OrderServiceImpl {
    @Autowired
    private OrderService orderService;

    /**
     * This method will return the username based on security context
     *
     * @return
     */
    private String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    /**
     * This method is used to place order.
     *
     * @param request
     * @return
     */
    @Transactional
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

    /**
     * This method is used to get the order details based on order ID
     *
     * @param orderID
     * @return
     */
    public OrderResponse getOrderDetails(String orderID) {
        try {
            return orderService.getOrderDetails(orderID);
        } catch (OrderException orderException) {
            log.error(orderException.getMessage());
            throw new OrderException(orderException.getMessage());
        }
    }

    /**
     * This method is used to get all order details
     *
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderResponse> getAllOrderDetails() {
        try {
            return orderService.getAllOrders();
        } catch (OrderException orderException) {
            log.error(orderException.getMessage());
            throw new OrderException(orderException.getMessage());
        }
    }

    /**
     * This method is used to update the order status like SHIPPED, DELIVERED etc.
     *
     * @param orderID
     * @param orderStatus
     * @return
     */
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public OrderResponse updateOrderStatus(String orderID, String orderStatus) {
        try {
            return orderService.updateOrderStatus(orderID, orderStatus);
        } catch (OrderException orderException) {
            log.error(orderException.getMessage());
            throw new OrderException(orderException.getMessage());
        }
    }

    /**
     * This method is used cancel the order based on order ID.
     *
     * @param orderID
     * @return
     */
    @Transactional
    @PreAuthorize("hasRole('USER')")
    public String cancelOrder(String orderID) {
        try {
            return orderService.cancelOrder(orderID);
        } catch (OrderException orderException) {
            log.error(orderException.getMessage());
            throw new OrderException(orderException.getMessage());
        }
    }

    /**
     * This method is used to get all cancelled orders for user
     *
     * @return
     */
    public List<OrderResponse> getAllCancelledOrders() {
        try {
            return orderService.getAllCancelledOrders(getUsername());
        } catch (OrderException orderException) {
            log.error(orderException.getMessage());
            throw new OrderException(orderException.getMessage());
        }
    }

    /**
     * This method is used to track the order based on tracking number
     *
     * @param trackingNumber
     * @return
     */
    public OrderResponse getOrderByTrackingNumber(String trackingNumber) {
        try {
            return orderService.getOrderByTrackingNumber(trackingNumber);
        } catch (OrderException orderException) {
            log.error(orderException.getMessage());
            throw new OrderException(orderException.getMessage());
        }
    }

    /**
     * This method is used to fetch all order based on username
     *
     * @return
     */
    public List<OrderResponse> getAllOrdersByUserName() {
        try {
            return orderService.getAllOrdersByUserName(getUsername());
        } catch (OrderException orderException) {
            log.error(orderException.getMessage());
            throw new OrderException(orderException.getMessage());
        }
    }
}
