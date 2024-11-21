package com.example.cart_service.Service.Impl;

import com.example.cart_service.DTO.Request.CartRequest;
import com.example.cart_service.DTO.Response.CartResponse;
import com.example.cart_service.Exception.CartException;
import com.example.cart_service.Helper.CartServiceHelper;
import com.example.cart_service.Mapper.CartMapper;
import com.example.cart_service.Model.Cart;
import com.example.cart_service.Repository.CartItemRepository;
import com.example.cart_service.Repository.CartRepository;
import com.example.cart_service.Service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartServiceHelper cartServiceHelper;

    @Autowired
    private CartMapper cartMapper;

    /**
     * This method will add the items to cart
     *
     * @param cartRequest
     * @return
     */
    @Override
    @Transactional
    public CartResponse addCartItems(CartRequest cartRequest) {
        boolean isCartPresentForUser = cartRepository.findByUsername(cartRequest.getUsername()).isPresent();
        if (!isCartPresentForUser) {
            Cart cart = cartServiceHelper.addItemsToCart(cartRequest);
            try {
                cartRepository.save(cart);
                log.info("Cart items are saved successfully");
                return cartMapper.toCartResponse(cart);
            } catch (Exception exception) {
                log.error("Unable to save the cart items");
                throw new CartException(exception.getMessage(), exception.getCause());
            }
        } else {
            return addCartItemsToExistingUser(cartRequest);
        }
    }

    /**
     * This method will fetch the cart item data from database
     *
     * @param username
     * @return
     */
    @Override
    public CartResponse getCartItems(String username) {
        Cart cart = cartRepository.findByUsername(username).orElseThrow(() -> {
            log.error("Cart items are not found the user {}", username);
            return new CartException("Cart Items not found.");
        });
        return cartMapper.toCartResponse(cart);
    }

    /**
     * This method will delete the products from cart
     *
     * @param username
     * @param productIDs
     * @return
     */
    @Override
    @Transactional
    public String deleteCartItems(String username, List<String> productIDs) {
        Cart cart = cartRepository.findByUsername(username).orElseThrow(() -> {
            log.error("Cart items are not found the user {}", username);
            return new CartException("Cart Items not found.");
        });
        List<String> failedDeletes = productIDs.stream().filter(productID -> {
            try {
                cartItemRepository.deleteProductFromCart(cart, productID);
                log.info("Item {} is deleted from cart", productID);
                return false; // Successful delete
            } catch (Exception e) {
                log.error("Unable to delete the item {} from cart. {}", productID, e.getMessage());
                return true; // Failed delete
            }
        }).collect(Collectors.toList());
        if (!failedDeletes.isEmpty()) {
            log.error("Failed to delete items : {}", failedDeletes);
            throw new CartException("Failed to delete items: " + failedDeletes);
        }
        cartServiceHelper.updateCartAmount(cart);
        cartRepository.updateByLastUpdateTime(Timestamp.from(Instant.now()), username);
        log.info("Total amount of the cart is updated successfully");
        return "Items have been deleted successfully";
    }

    /**
     * This method will clear all products from cart
     *
     * @param username
     * @return
     */
    @Override
    @Transactional
    public String emptyCart(String username) {
        Cart cart = cartRepository.findByUsername(username).orElseThrow(() -> {
            log.error("Cart items are not found for the user {}", username);
            return new CartException("Username is not found.");
        });
        try {
            cartItemRepository.deleteByCart(cart);
            cartRepository.delete(cart);
            log.info("Cart Items are deleted successfully");
        } catch (Exception exception) {
            log.error("Unable to delete the cart items. {}", exception.getMessage());
            throw new CartException(exception.getMessage());
        }
        return "Cart is empty now.";
    }

    /**
     * This method will return the total amount of the cart
     *
     * @param username
     * @return
     */
    @Override
    public double getTotalCartAmount(String username) {
        Cart cart = cartRepository.findByUsername(username).orElseThrow(() -> {
            log.error("Cart items are not found for the user {}", username);
            return new CartException("User not found");
        });
        return cart.getTotalAmount();
    }

    /**
     * This method will add the items to cart for existing customer
     *
     * @param cartRequest
     * @return
     */
    @Override
    public CartResponse addCartItemsToExistingUser(CartRequest cartRequest) {
        Cart cart = cartRepository.findByUsername(cartRequest.getUsername()).orElseThrow(() -> {
            log.error("Cart items are not found for the user {}", cartRequest.getUsername());
            return new CartException("Cart items not found for this user");
        });

        Map<String, List<String>> productIDs = cartServiceHelper.getExistingAndNewProductFromRequest(cart, cartRequest);
        List<String> sameProductIDs = productIDs.get("sameProductIDs");
        List<String> differentProductIDs = productIDs.get("differentProductIDs");
        if (!sameProductIDs.isEmpty()) {
            cartServiceHelper.addProductsToCart(sameProductIDs, cartRequest, cart, "SAME");
        }
        if (!differentProductIDs.isEmpty()) {
            cartServiceHelper.addProductsToCart(differentProductIDs, cartRequest, cart, "DIFF");
        }
        return getCartItems(cartRequest.getUsername());
    }

}
