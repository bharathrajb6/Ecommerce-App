package com.example.cart_service.Service;

import com.example.cart_service.DTO.Request.CartRequest;
import com.example.cart_service.DTO.Request.ProductRequest;
import com.example.cart_service.DTO.Response.CartResponse;

public interface CartService {
    CartResponse addCartItems(CartRequest cartRequest);

    CartResponse getCartItems(String username);

    CartResponse updateCartItems(ProductRequest request);

    CartResponse deleteCartItems(String productID);
}
