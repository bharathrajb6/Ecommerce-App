package com.example.analytics_reporting_service.controller;


import com.example.analytics_reporting_service.dto.response.SalesResponse;
import com.example.analytics_reporting_service.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/report/")
public class SalesReportController {

    @Autowired
    private SalesService salesService;

    @RequestMapping(value = "/sales", method = RequestMethod.GET)
    public SalesResponse getSales() {
        return salesService.getTotalNumberOfSales();
    }

    @RequestMapping(value = "/sales/filter", method = RequestMethod.GET)
    public SalesResponse getSalesFilter(@RequestParam("start") String start, @RequestParam("end") String end) {
        return salesService.getOrdersFilter(start, end);
    }

    @RequestMapping(value = "/sales/revenue", method = RequestMethod.GET)
    public SalesResponse getTotalRevenue() {
        return salesService.getTotalRevenue();
    }

    @RequestMapping(value = "/sales/revenue/filter", method = RequestMethod.GET)
    public SalesResponse getTotalRevenueFilter(@RequestParam("start") String start, @RequestParam("end") String end) {
        return salesService.getTotalRevenueFilter(start, end);
    }

    @RequestMapping(value = "/sales/order/cancelled", method = RequestMethod.GET)
    public SalesResponse getTotalRevenueFilter() {
        return salesService.getAllCancelledOrders();
    }

}
