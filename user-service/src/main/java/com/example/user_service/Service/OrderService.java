package com.example.user_service.Service;

import com.example.user_service.DTO.Request.OrderRequest;
import com.example.user_service.DTO.Response.OrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "order-service")
public interface OrderService {

    @RequestMapping(value = "/api/v1/order", method = RequestMethod.POST)
    OrderResponse placeOrder(@RequestBody OrderRequest request);

    @RequestMapping(value = "/api/v1/order/{orderID}", method = RequestMethod.GET)
    OrderResponse getOrderDetails(@PathVariable String orderID);

    @RequestMapping(value = "/api/v1/order", method = RequestMethod.GET)
    List<OrderResponse> getAllOrders();

    @RequestMapping(value = "/api/v1/order/{orderID}/status", method = RequestMethod.PUT)
    OrderResponse updateOrderStatus(@PathVariable String orderID, @RequestBody String orderStatus);

    @RequestMapping(value = "/api/v1/order/{orderID}/cancel", method = RequestMethod.PUT)
    String cancelOrder(@PathVariable String orderID);

    @RequestMapping(value = "/api/v1/order/{username}/cancelled", method = RequestMethod.GET)
    List<OrderResponse> getAllCancelledOrders(@PathVariable String username);

    @RequestMapping(value = "/api/v1/order/track/{trackingNumber}", method = RequestMethod.GET)
    OrderResponse getOrderByTrackingNumber(@PathVariable String trackingNumber);

    @RequestMapping(value = "/api/v1/order/search/{username}", method = RequestMethod.GET)
    List<OrderResponse> getAllOrdersByUserName(@PathVariable String username);

}
