package com.example.analytics_reporting_service.controller;


import com.example.analytics_reporting_service.dto.response.SalesResponse;
import com.example.analytics_reporting_service.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api/v1/report/")
public class SalesReportController {

    @Autowired
    private SalesService salesService;

    @RequestMapping(value = "/sales", method = RequestMethod.GET)
    public SalesResponse getSales() {
        return salesService.getTotalNumberOfSales();
    }

    @RequestMapping(value = "/sales/revenue", method = RequestMethod.GET)
    public SalesResponse getTotalRevenue() {
        return new SalesResponse();
    }

    @RequestMapping(value = "/sales/orders", method = RequestMethod.GET)
    public SalesResponse SalesResponse() {
        return new SalesResponse();
    }

}
