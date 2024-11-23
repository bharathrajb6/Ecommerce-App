package com.example.analytics_reporting_service.service;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "order-service")
public interface OrderService {

    @RequestMapping(value = "/api/v1/order/totalOrders", method = RequestMethod.GET)
    public int getTotalOrders();

}
