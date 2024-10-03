package com.example.order_service.Controller;

import com.example.order_service.DTO.Request.OrderRequest;
import com.example.order_service.DTO.Response.OrderResponse;
import com.example.order_service.Service.OrderService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private OrderService orderService;

    public OrderResponse placeOrder(@RequestBody OrderRequest request){
        return orderService.placeOrder(request);
    }

}
