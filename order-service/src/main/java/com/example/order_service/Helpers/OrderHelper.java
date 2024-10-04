package com.example.order_service.Helpers;

import com.example.order_service.DTO.Request.OrderItemRequest;
import com.example.order_service.DTO.Request.OrderRequest;
import com.example.order_service.DTO.Response.ProductResponse;
import com.example.order_service.Exception.OrderException;
import com.example.order_service.Mapper.OrderItemMapper;
import com.example.order_service.Mapper.OrderMapper;
import com.example.order_service.Model.OrderItems;
import com.example.order_service.Model.OrderStatus;
import com.example.order_service.Model.Orders;
import com.example.order_service.Service.ProductService;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class OrderHelper {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private ProductService productService;

    public Orders generateOrder(OrderRequest request) {
        checkIfAllProductsStocks(request.getOrderItems());
        Orders order = orderMapper.toOrders(request);
        order.setOrderID(UUID.randomUUID().toString());
        order.setUsername("bharath");
        order.setOrderStatus(OrderStatus.PLACED);
        order.setTrackingNumber("TRACK" + UUID.randomUUID().toString());
        order.setTotalAmount(calculateTotalAmount(request.getOrderItems()));
        order.setCreatedAt(Timestamp.from(Instant.now()));
        order.setUpdatedAt(Timestamp.from(Instant.now()));
        String paymentMethod = order.getPaymentMethod();
        order.setPaymentStatus(getPaymentStatus(paymentMethod));
        List<OrderItems> orderItems = order.getOrderItems();
        for (OrderItems item : orderItems) {
            ProductResponse product = productService.getProduct(item.getProductId());
            item.setOrderItemId(UUID.randomUUID().toString());
            item.setOrder(order);
            item.setPrice((double) product.getPrice());
            item.setTotalPrice(item.getPrice() * item.getQuantity());
        }
        return order;
    }

    private Double calculateTotalAmount(List<OrderItemRequest> orderItemRequestList) {
        return orderItemRequestList.stream().mapToDouble(itemRequest -> {
            ProductResponse product = productService.getProduct(itemRequest.getProductId());
            double itemTotal = product.getPrice() * itemRequest.getQuantity();
            productService.updateProductStock(itemRequest.getProductId(), product.getStock() - itemRequest.getQuantity());
            return itemTotal;
        }).sum();
    }


    private void checkIfAllProductsStocks(List<OrderItemRequest> orderItems) {
        orderItems.forEach(item -> {
            try {
                int stock = productService.getStock(item.getProductId());
                if (item.getQuantity() > stock) {
                    throw new OrderException("Stock is there");
                }
            } catch (FeignException exception) {
                throw new OrderException(exception.getMessage());
            }
        });
    }

    private String getPaymentStatus(String paymentMethod) {
        switch (paymentMethod) {
            case "POD":
                return "Pending";
            case "Credit Card":
            case "Debit Card":
                return "Completed";
            default:
                return "Unknown";
        }
    }
}
