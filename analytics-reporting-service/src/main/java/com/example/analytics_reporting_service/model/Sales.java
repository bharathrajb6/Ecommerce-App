package com.example.analytics_reporting_service.model;

import com.example.analytics_reporting_service.dto.response.OrderResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sales {
    private int totalSales;
    private double totalRevenue;
    private List<OrderResponse> ordersList;
}
