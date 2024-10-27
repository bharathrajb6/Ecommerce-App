package com.example.cart_service.Controller;

import com.example.cart_service.DTO.Request.CartRequest;
import com.example.cart_service.DTO.Response.CartResponse;
import com.example.cart_service.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class CartServiceController {

    @Autowired
    private CartService cartService;

    public CartResponse addCartItem(@RequestBody CartRequest cartRequest) {
            return cartService.addCartItems(cartRequest);
    }
}
