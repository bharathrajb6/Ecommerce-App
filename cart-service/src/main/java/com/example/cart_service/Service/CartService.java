package com.example.cart_service.Service;

import com.example.cart_service.DTO.Request.CartItemsRequest;
import com.example.cart_service.DTO.Request.CartRequest;
import com.example.cart_service.DTO.Response.CartResponse;

import java.util.List;

public interface CartService {
    CartResponse addCartItems(CartRequest cartRequest);

    CartResponse getCartItems(String username);

    CartResponse updateCartItems(CartItemsRequest request);

    String deleteCartItems(String username,List<String> productID);

    boolean emptyCart(String username);
}
