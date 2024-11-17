package com.example.cart_service.Helper;

import com.example.cart_service.DTO.Request.CartItemsRequest;
import com.example.cart_service.DTO.Request.CartRequest;
import com.example.cart_service.DTO.Response.ProductResponse;
import com.example.cart_service.Mapper.CartMapper;
import com.example.cart_service.Model.Cart;
import com.example.cart_service.Model.CartItems;
import com.example.cart_service.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class CartServiceHelper {

    @Autowired
    private ProductService productService;

    @Autowired
    private CartMapper cartMapper;

    public Cart addItemsToCart(CartRequest cartRequest) {
        boolean stocksPresent = checkIfProductsAreFound(cartRequest.getCartItemsList());
        if (stocksPresent) {
            Cart cart = cartMapper.toCart(cartRequest);
            cart.setUsername("bharath");
            cart.setCartID(UUID.randomUUID().toString());
            cart.setCreatedAt(Timestamp.from(Instant.now()));
            cart.setUpdatedAt(Timestamp.from(Instant.now()));
            List<CartItems> cartItems = new ArrayList<CartItems>();
            for (CartItemsRequest cartItemsRequest : cartRequest.getCartItemsList()) {
                CartItems cartItem = new CartItems();
                ProductResponse product = productService.getProduct(cartItemsRequest.getProdID());
                cartItem.setCartItemID(UUID.randomUUID().toString());
                cartItem.setProdID(product.getProdID());
                cartItem.setQuantity(cartItemsRequest.getQuantity());
                cartItem.setPrice((double) product.getPrice());
                cartItem.setTotalPrice((double) (product.getPrice() * cartItemsRequest.getQuantity()));
                cartItem.setCart(cart);
                cartItems.add(cartItem);
            }
            cart.setCartItemsList(cartItems);
            cart.setTotalAmount(calculateTotalAmount(cartItems));
            return cart;
        } else {
            return null;
        }
    }

    private boolean checkIfProductsAreFound(List<CartItemsRequest> productRequestList) {
        boolean isPresent = true;
        for (CartItemsRequest productRequest : productRequestList) {
            int stock = productService.getStock(productRequest.getProdID());
            if (!(productRequest.getQuantity() <= stock)) {
                isPresent = false;
                return isPresent;
            }
        }
        return isPresent;
    }

    private double calculateTotalAmount(List<CartItems> cartItems) {
        return cartItems.stream().mapToDouble(cart->{
            return cart.getTotalPrice();
                }
        ).sum();
    }
}
