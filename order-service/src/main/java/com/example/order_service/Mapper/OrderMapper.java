package com.example.order_service.Mapper;

import com.example.order_service.DTO.Request.OrderRequest;
import com.example.order_service.DTO.Response.OrderResponse;
import com.example.order_service.Model.Orders;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    Orders toOrders(OrderRequest request);

    OrderResponse toOrderResponse(Orders orders);

    List<OrderResponse> toOrderResponseList(List<Orders> ordersList);
}
