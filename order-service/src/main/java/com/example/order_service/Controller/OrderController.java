package com.example.order_service.Controller;

import com.example.order_service.DTO.Request.OrderRequest;
import com.example.order_service.DTO.Response.OrderResponse;
import com.example.order_service.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/order",method = RequestMethod.POST)
    public OrderResponse placeOrder(@RequestBody OrderRequest request){
        return orderService.placeOrder(request);
    }

    @RequestMapping(value = "/order/{orderID}",method = RequestMethod.GET)
    public OrderResponse placeOrder(@PathVariable String orderID){
        return orderService.getOrderDetails(orderID);
    }
}
