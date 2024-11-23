package com.example.analytics_reporting_service.service.impl;

import com.example.analytics_reporting_service.dto.response.SalesResponse;
import com.example.analytics_reporting_service.mapper.SalesMapper;
import com.example.analytics_reporting_service.model.Sales;
import com.example.analytics_reporting_service.service.OrderService;
import com.example.analytics_reporting_service.service.SalesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
