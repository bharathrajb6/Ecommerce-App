package com.example.analytics_reporting_service.service;

import com.example.analytics_reporting_service.dto.response.SalesResponse;
import org.springframework.stereotype.Service;

@Service
public interface SalesService {

    SalesResponse getTotalNumberOfSales();

    SalesResponse getOrdersFilter(String start, String end);

    SalesResponse getTotalRevenue();

    SalesResponse getTotalRevenueFilter(String start, String end);

    SalesResponse getAllCancelledOrders();

}
