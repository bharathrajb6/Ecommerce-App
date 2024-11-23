package com.example.analytics_reporting_service.mapper;

import com.example.analytics_reporting_service.dto.response.SalesResponse;
import com.example.analytics_reporting_service.model.Sales;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SalesMapper {

     SalesResponse toSalesResponse(Sales sales);
}
