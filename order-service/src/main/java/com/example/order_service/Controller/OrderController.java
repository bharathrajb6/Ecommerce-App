package com.example.order_service.Controller;

import com.example.order_service.DTO.Request.OrderRequest;
import com.example.order_service.DTO.Response.OrderResponse;
import com.example.order_service.Service.OrderService;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public OrderResponse placeOrder(@RequestBody OrderRequest request) {
        return orderService.placeOrder(request);
    }

    @RequestMapping(value = "/order/{orderID}", method = RequestMethod.GET)
    public OrderResponse getOrderDetails(@PathVariable String orderID) {
        return orderService.getOrderDetails(orderID);
    }

    @RequestMapping(value = "/order", method = RequestMethod.GET)
    public List<OrderResponse> getAllOrders() {
        return orderService.getAllOrders();
    }

    @RequestMapping(value = "/order/{orderID}/status", method = RequestMethod.PUT)
    public OrderResponse updateOrderStatus(@PathVariable String orderID, @RequestBody String orderStatus) {
        return orderService.updateOrderStatus(orderID, orderStatus);
    }

    @RequestMapping(value = "/order/cancel/{orderID}", method = RequestMethod.PUT)
    public String cancelOrder(@PathVariable String orderID) {
        return orderService.cancelOrder(orderID);
    }

    @RequestMapping(value = "/order/cancelled/{username}", method = RequestMethod.GET)
    public List<OrderResponse> getAllCancelledOrders(@PathVariable String username) {
        return orderService.getAllCancelledOrders(username);
    }

    @RequestMapping(value = "/order/track/{trackingNumber}", method = RequestMethod.GET)
    public OrderResponse getOrderByTrackingNumber(@PathVariable String trackingNumber) {
        return orderService.getOrderStatusByTrackNumber(trackingNumber);
    }

    @RequestMapping(value = "/order/search/{username}",method = RequestMethod.GET)
    public List<OrderResponse> getAllOrdersByUserName(@PathVariable String username){
        return orderService.getAllOrdersByUserName(username);
    }
}
