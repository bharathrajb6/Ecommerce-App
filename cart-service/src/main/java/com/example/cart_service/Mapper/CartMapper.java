package com.example.cart_service.Mapper;

import com.example.cart_service.DTO.Request.CartItemsRequest;
import com.example.cart_service.DTO.Request.CartRequest;
import com.example.cart_service.DTO.Response.CartResponse;
import com.example.cart_service.DTO.Response.CartItemResponse;
import com.example.cart_service.Model.Cart;
import com.example.cart_service.Model.CartItems;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {

    Cart toCart(CartRequest cartRequest);

    CartResponse toCartResponse(Cart cart);

    CartItems toCartItems(CartItemsRequest productRequest);

    CartItemResponse toProductResponse(CartItems cartItems);
}
