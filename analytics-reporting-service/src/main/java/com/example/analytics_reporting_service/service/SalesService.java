package com.example.analytics_reporting_service.service;

import com.example.analytics_reporting_service.dto.response.SalesResponse;
import org.springframework.stereotype.Service;

@Service
public interface SalesService {

    SalesResponse getTotalNumberOfSales();
}
