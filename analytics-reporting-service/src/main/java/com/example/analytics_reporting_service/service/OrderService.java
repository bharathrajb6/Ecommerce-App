package com.example.analytics_reporting_service.service;


import com.example.analytics_reporting_service.dto.response.OrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "order-service")
public interface OrderService {

    @RequestMapping(value = "/api/v1/order/totalOrders", method = RequestMethod.GET)
    int getTotalOrders();

    @RequestMapping(value = "/api/v1/order/filter", method = RequestMethod.GET)
    List<OrderResponse> getOrdersFilter(@RequestParam("start") String start, @RequestParam("end") String end);

    @RequestMapping(value = "/api/v1/order/cancelled", method = RequestMethod.GET)
    int getAllCancelledOrders();

    @RequestMapping(value = "/api/v1/order/revenue", method = RequestMethod.GET)
    double getTotalRevenue();

    @RequestMapping(value = "/api/v1/order/revenue/filter", method = RequestMethod.GET)
    double getTotalRevenueFilter(@RequestParam("start") String start, @RequestParam("end") String end);

}
