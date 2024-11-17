package com.example.cart_service.Service.Impl;

import com.example.cart_service.DTO.Request.CartItemsRequest;
import com.example.cart_service.DTO.Request.CartRequest;
import com.example.cart_service.DTO.Response.CartResponse;
import com.example.cart_service.Exception.CartException;
import com.example.cart_service.Helper.CartServiceHelper;
import com.example.cart_service.Mapper.CartMapper;
import com.example.cart_service.Model.Cart;
import com.example.cart_service.Repository.CartRepository;
import com.example.cart_service.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartServiceHelper cartServiceHelper;

    @Autowired
    private CartMapper cartMapper;

    @Override
    @Transactional
    public CartResponse addCartItems(CartRequest cartRequest) {
        Cart cart = cartServiceHelper.addItemsToCart(cartRequest);
        try{
            cartRepository.save(cart);
            return cartMapper.toCartResponse(cart);
        } catch (Exception exception){
            throw new CartException(exception.getMessage(),exception.getCause());
        }
    }

    @Override
    public CartResponse getCartItems(String username) {
        Cart cart = cartRepository.findByUsername(username);
        return cartMapper.toCartResponse(cart);
    }

    @Override
    public CartResponse updateCartItems(CartItemsRequest request) {
        return null;
    }

    @Override
    public CartResponse deleteCartItems(List<String > productID) {
        return null;
    }
}
