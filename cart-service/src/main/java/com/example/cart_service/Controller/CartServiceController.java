package com.example.cart_service.Controller;

import com.example.cart_service.DTO.Request.CartRequest;
import com.example.cart_service.DTO.Response.CartResponse;
import com.example.cart_service.Service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CartServiceController {

    @Autowired
    private CartService cartService;

    /**
     * This method is used to add the items to cart
     *
     * @param cartRequest
     * @return
     */
    @Operation(summary = "Add Items to cart", description = "Add items to cart")
    @RequestMapping(value = "/cart", method = RequestMethod.POST)
    public CartResponse addCartItem(@RequestBody CartRequest cartRequest) {
        return cartService.addCartItems(cartRequest);
    }

    /**
     * This method is used to get the cart items for the specific user
     *
     * @param username
     * @return
     */
    @Operation(summary = "Get Cart Items", description = "Get Cart Items")
    @RequestMapping(value = "/cart/{username}", method = RequestMethod.GET)
    public CartResponse getCartItems(@PathVariable String username) {
        return cartService.getCartItems(username);
    }

    /**
     * This method is used to delete the cart items for specific user
     *
     * @param username
     * @param prodIDs
     * @return
     */
    @Operation(summary = "Delete Cart Items", description = "Delete the items from cart")
    @RequestMapping(value = "/cart/{username}", method = RequestMethod.DELETE)
    public CartResponse deleteCartItems(@PathVariable String username, @RequestBody List<String> prodIDs) {
        return cartService.deleteCartItems(username, prodIDs);
    }

    /**
     * This method is used to clear all items from cart
     *
     * @param username
     * @return
     */
    @Operation(summary = "Clear Cart", description = "Clear all items from cart")
    @RequestMapping(value = "/cart/{username}/clear", method = RequestMethod.DELETE)
    public String emptyCart(@PathVariable String username) {
        return cartService.emptyCart(username);
    }

    /**
     * This method is used to get the total amount of cart
     *
     * @param username
     * @return
     */
    @Operation(summary = "Get Total Cart Amount", description = "Get total cart amount")
    @RequestMapping(value = "/cart/{username}/totalCartAmount", method = RequestMethod.GET)
    public double getTotalCartAmount(String username) {
        return cartService.getTotalCartAmount(username);
    }
}
