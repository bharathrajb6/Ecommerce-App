package com.example.cart_service.Service.Impl;

import com.example.cart_service.DTO.Request.CartRequest;
import com.example.cart_service.DTO.Request.ProductRequest;
import com.example.cart_service.DTO.Response.CartResponse;
import com.example.cart_service.Repository.CartRepository;
import com.example.cart_service.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Override
    public CartResponse addCartItems(CartRequest cartRequest) {
        return null;
    }

    @Override
    public CartResponse getCartItems(String username) {
        return null;
    }

    @Override
    public CartResponse updateCartItems(ProductRequest request) {
        return null;
    }

    @Override
    public CartResponse deleteCartItems(String productID) {
        return null;
    }
}
