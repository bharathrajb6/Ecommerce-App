package com.example.cart_service.Mapper;

import com.example.cart_service.DTO.Response.CartItemResponse;
import com.example.cart_service.Model.CartItems;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    List<CartItemResponse> toCartItemResponse(List<CartItems> items);

}
