package com.example.analytics_reporting_service.service.impl;

import com.example.analytics_reporting_service.dto.response.OrderResponse;
import com.example.analytics_reporting_service.dto.response.SalesResponse;
import com.example.analytics_reporting_service.mapper.SalesMapper;
import com.example.analytics_reporting_service.model.Sales;
import com.example.analytics_reporting_service.service.OrderService;
import com.example.analytics_reporting_service.service.SalesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SalesServiceImpl implements SalesService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private SalesMapper salesMapper;

    @Override
    public SalesResponse getTotalNumberOfSales() {
        int totalSales = orderService.getTotalOrders();
        return salesMapper.toSalesResponse(Sales.builder().totalSales(totalSales).build());
    }

    @Override
    public SalesResponse getOrdersFilter(String start, String end) {
        List<OrderResponse> orderResponseList = orderService.getOrdersFilter(start, end);
        double revenue = orderResponseList.stream().mapToDouble(OrderResponse::getTotalAmount).sum();
        return salesMapper.toSalesResponse(Sales.builder().
                totalSales(orderResponseList.size()).
                totalRevenue(revenue).
                ordersList(orderResponseList).
                build());
    }

    @Override
    public SalesResponse getTotalRevenue() {
        double totalRevenue = orderService.getTotalRevenue();
        return salesMapper.toSalesResponse(Sales.builder().totalRevenue(totalRevenue).build());
    }

    @Override
    public SalesResponse getTotalRevenueFilter(String start, String end) {
        double revenue = orderService.getTotalRevenueFilter(start, end);
        return salesMapper.toSalesResponse(Sales.builder().totalRevenue(revenue).build());
    }


    @Override
    public SalesResponse getAllCancelledOrders() {
        int totalSales = orderService.getAllCancelledOrders();
        return salesMapper.toSalesResponse(Sales.builder().totalSales(totalSales).build());
    }

}
