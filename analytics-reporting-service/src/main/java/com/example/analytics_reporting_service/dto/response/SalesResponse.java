package com.example.analytics_reporting_service.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesResponse {
    private int totalSales;
    private int totalRevenue;
}
