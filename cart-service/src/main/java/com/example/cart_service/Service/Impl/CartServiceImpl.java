package com.example.cart_service.Service.Impl;

import com.example.cart_service.DTO.Request.CartItemsRequest;
import com.example.cart_service.DTO.Request.CartRequest;
import com.example.cart_service.DTO.Response.CartResponse;
import com.example.cart_service.Exception.CartException;
import com.example.cart_service.Helper.CartServiceHelper;
import com.example.cart_service.Mapper.CartMapper;
import com.example.cart_service.Model.Cart;
import com.example.cart_service.Repository.CartItemRepository;
import com.example.cart_service.Repository.CartRepository;
import com.example.cart_service.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartServiceHelper cartServiceHelper;

    @Autowired
    private CartMapper cartMapper;

    @Override
    @Transactional
    public CartResponse addCartItems(CartRequest cartRequest) {
        Cart cart = cartServiceHelper.addItemsToCart(cartRequest);
        try {
            cartRepository.save(cart);
            return cartMapper.toCartResponse(cart);
        } catch (Exception exception) {
            throw new CartException(exception.getMessage(), exception.getCause());
        }
    }

    @Override
    public CartResponse getCartItems(String username) {
        Cart cart = cartRepository.findByUsername(username).orElseThrow(() -> {
            return new CartException("Cart Items not found.");
        });
        return cartMapper.toCartResponse(cart);
    }

    @Override
    public CartResponse updateCartItems(CartItemsRequest request) {
        return null;
    }

    @Override
    public String deleteCartItems(String username, List<String> productIDs) {
        Cart cart = cartRepository.findByUsername(username).orElseThrow(() -> {
            return new CartException("Cart Items not found.");
        });
        List<String> failedDeletes = productIDs.stream()
                .filter(productID -> {
                    try {
                        cartItemRepository.deleteProductFromCart(cart, productID);
                        return false; // Successful delete
                    } catch (Exception e) {
                        return true; // Failed delete
                    }
                })
                .collect(Collectors.toList());
        if (!failedDeletes.isEmpty()) {
            throw new CartException("Failed to delete items: " + failedDeletes);
        }
        return "Items have been deleted successfully";
    }
}
