package com.example.cart_service.Service;

import com.example.cart_service.DTO.Request.CartRequest;
import com.example.cart_service.DTO.Response.CartResponse;

import java.util.List;

public interface CartService {
    CartResponse addCartItems(CartRequest cartRequest);

    CartResponse getCartItems(String username);

    CartResponse deleteCartItems(String username, List<String> productID);

    String emptyCart(String username);

    double getTotalCartAmount(String username);

    CartResponse addCartItemsToExistingUser(CartRequest cartRequest);
}
