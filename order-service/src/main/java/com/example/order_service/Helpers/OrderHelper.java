package com.example.order_service.Helpers;

import com.example.order_service.DTO.Request.OrderItemRequest;
import com.example.order_service.DTO.Request.OrderRequest;
import com.example.order_service.DTO.Response.OrderResponse;
import com.example.order_service.DTO.Response.ProductResponse;
import com.example.order_service.Exception.OrderException;
import com.example.order_service.Mapper.OrderItemMapper;
import com.example.order_service.Mapper.OrderMapper;
import com.example.order_service.Model.OrderItems;
import com.example.order_service.Model.OrderStatus;
import com.example.order_service.Model.Orders;
import com.example.order_service.Service.Impl.RedisServiceImpl;
import com.example.order_service.Service.ProductService;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.order_service.Messages.OrderMessages.*;

@Service
@Slf4j
public class OrderHelper {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private ProductService productService;

    @Autowired
    private RedisServiceImpl redisService;

    private static final String cancelled = "CANCELLED";

    /***
     * This method is used to generate order
     * @param request
     * @return
     */
    public Orders generateOrder(OrderRequest request) {
        // This method is used to check if all products have requested quantity
        checkIfAllProductsStocks(request.getOrderItems());

        // Convert the order request to order
        Orders order = orderMapper.toOrders(request);

        // Generate the order ID using UUID
        String id = UUID.randomUUID().toString();
        order.setUsername(request.getUsername());
        order.setOrderID(id);
        order.setOrderStatus(OrderStatus.PLACED);
        order.setTrackingNumber("TRACK" + id);

        // Calculate the total amount of all products
        order.setTotalAmount(calculateTotalAmount(request.getOrderItems()));

        Timestamp currentTimeStamp = Timestamp.from(Instant.now());
        order.setCreatedAt(currentTimeStamp);
        order.setUpdatedAt(currentTimeStamp);

        String paymentMethod = order.getPaymentMethod();
        order.setPaymentStatus(getPaymentStatus(paymentMethod));

        List<OrderItems> orderItems = order.getOrderItems();
        for (OrderItems item : orderItems) {
            // Get the product details from cache. If it is not present in cache, then get the data from product service
            ProductResponse product = redisService.getData(item.getProductId(), ProductResponse.class);
            if (product == null) {
                product = productService.getProduct(item.getProductId());
            }
            item.setOrderItemId(UUID.randomUUID().toString());
            item.setOrder(order);
            item.setPrice((double) product.getPrice());
            item.setTotalPrice(item.getPrice() * item.getQuantity());
        }

        log.info(ORDER_CREATED);
        return order;
    }

    /***
     * This method is used to calculate total amount
     * @param orderItemRequestList
     * @return
     */
    private Double calculateTotalAmount(List<OrderItemRequest> orderItemRequestList) {
        return orderItemRequestList.stream().mapToDouble(itemRequest -> {
            ProductResponse product = redisService.getData(itemRequest.getProductId(), ProductResponse.class);
            if (product == null) {
                product = productService.getProduct(itemRequest.getProductId());
            }
            double itemTotal = product.getPrice() * (double) itemRequest.getQuantity();
            // Update the product stock after order
            productService.updateProductStock(itemRequest.getProductId(), product.getStock() - itemRequest.getQuantity());
            return itemTotal;
        }).sum();
    }


    /***
     * This method is used to check if all products have stocks
     * @param orderItems
     */
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

    /***
     * This method is used to get payment status
     * @param paymentMethod
     * @return
     */
    private String getPaymentStatus(String paymentMethod) {
        return switch (paymentMethod) {
            case "POD" -> "Pending";
            case "Credit Card", "Debit Card" -> "Completed";
            default -> "Unknown";
        };
    }

    /***
     * This method is used to update product stock after cancel
     * @param orderItems
     */
    public void updateProductAfterCancel(List<OrderItems> orderItems) {
        for (OrderItems item : orderItems) {
            try {
                ProductResponse product = productService.getProduct(item.getProductId());
                productService.updateProductStock(item.getProductId(), product.getStock() + item.getQuantity());
                redisService.deleteData(product.getProdID());
                log.info(PRODUCT_UPDATED_AFTER_ORDER_CANCEL, product.getProdName());
            } catch (FeignException exception) {
                log.error(UNABLE_UPDATE_PRODUCT_AFTER_ORDER_CANCEL, item.getProductId());
                throw new OrderException(exception.getMessage());
            }
        }
    }

    /**
     * This method is used to filter the orders based on start date and end date
     *
     * @param start
     * @param end
     * @param ordersList
     * @return
     */
    public List<OrderResponse> filterOrders(String start, String end, List<OrderResponse> ordersList) {
        LocalDate startDate;
        LocalDate endDate;
        try {
            startDate = LocalDate.parse(start);
            endDate = LocalDate.parse(end);
        } catch (DateTimeParseException parseException) {
            throw new OrderException(parseException.getMessage());
        }
        List<OrderResponse> filteredOrderList = new ArrayList<>();
        for (OrderResponse orders : ordersList) {
            Timestamp orderTimeStamp = orders.getCreatedAt();
            LocalDate date = orderTimeStamp.toLocalDateTime().toLocalDate();
            if ((date.isAfter(startDate) && date.isBefore(endDate)) || date.equals(startDate) || date.equals(endDate)) {
                filteredOrderList.add(orders);
            }
        }
        return filteredOrderList;
    }

    /**
     * Filters the orders based on the operation type.
     *
     * @param orderResponses List of OrderResponse
     * @param operationType  The operation type to filter by
     * @return Filtered list of OrderResponse
     */
    public List<OrderResponse> filterOrderResponses(List<OrderResponse> orderResponses, String operationType) {
        if (orderResponses == null || orderResponses.isEmpty()) {
            return Collections.emptyList();
        }
        if (cancelled.equals(operationType)) {
            return orderResponses.stream().filter(orderResponse -> "cancelled".equals(orderResponse.getOrderStatus())).collect(Collectors.toList());
        }
        return orderResponses;
    }
}
