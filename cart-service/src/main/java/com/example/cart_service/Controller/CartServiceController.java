package com.example.cart_service.Controller;

import com.example.cart_service.DTO.Request.CartItemsRequest;
import com.example.cart_service.DTO.Request.CartRequest;
import com.example.cart_service.DTO.Response.CartResponse;
import com.example.cart_service.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CartServiceController {

    @Autowired
    private CartService cartService;

    @RequestMapping(value = "/cart", method = RequestMethod.POST)
    public CartResponse addCartItem(@RequestBody CartRequest cartRequest) {
        return cartService.addCartItems(cartRequest);
    }

    @RequestMapping(value = "/cart/{username}", method = RequestMethod.GET)
    public CartResponse getCartItems(@PathVariable String username) {
        return cartService.getCartItems(username);
    }

    @RequestMapping(value = "/cart", method = RequestMethod.PUT)
    public CartResponse updateCarItems(@RequestBody CartItemsRequest cartItemsRequest) {
        return cartService.updateCartItems(cartItemsRequest);
    }

    @RequestMapping(value = "/cart/{username}", method = RequestMethod.DELETE)
    public String deleteCartItems(@PathVariable String username, @RequestBody List<String> prodIDs) {
        return cartService.deleteCartItems(username,prodIDs);
    }
}
