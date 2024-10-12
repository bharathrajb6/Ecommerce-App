package com.example.order_service.Mapper;

import com.example.order_service.DTO.Response.OrderItemResponse;
import com.example.order_service.Model.OrderItems;
import org.mapstruct.Mapper;

import java.util.List;

/***
 * This is the OrderItemMapper interface
 */
@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    List<OrderItemResponse> toOrderItemResponse(List<OrderItems> items);
}
