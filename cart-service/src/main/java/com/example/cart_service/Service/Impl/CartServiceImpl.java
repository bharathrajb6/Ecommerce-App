package com.example.cart_service.Service.Impl;

import com.example.cart_service.DTO.Request.CartRequest;
import com.example.cart_service.DTO.Response.CartItemResponse;
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

import static com.example.cart_service.Messages.CartExceptionMessages.EXCEPTION_CART_ITEMS_NOT_FOUND_FOR_USER;
import static com.example.cart_service.Messages.CartExceptionMessages.EXCEPTION_FAILED_TO_DELETE_ITEMS_FROM_CART;
import static com.example.cart_service.Messages.CartLogMessages.*;

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

    @Autowired
    private RedisServiceImpl redisService;

    private static final String ITEMS_DELETED_SUCCESSFULLY = "Items have been deleted successfully";

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
                log.info(CART_ITEMS_SAVED_SUCCESSFULLY);
                redisService.deleteData("cartList");
                return cartMapper.toCartResponse(cart);
            } catch (Exception exception) {
                log.error(UNABLE_TO_SAVE_CART_ITEMS);
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
        String key = "cart" + username;
        CartResponse cartResponse = redisService.getData(key, CartResponse.class);
        if (cartResponse != null) {
            return cartResponse;
        }
        Cart cart = cartRepository.findByUsername(username).orElseThrow(() -> {
            log.error(CART_ITEMS_NOT_FOUND_FOR_USER, username);
            return new CartException(EXCEPTION_CART_ITEMS_NOT_FOUND_FOR_USER + username);
        });
        cartResponse = cartMapper.toCartResponse(cart);
        redisService.setData(key, cartResponse, 300L);
        return cartResponse;
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
    public CartResponse deleteCartItems(String username, List<String> productIDs) {
        Cart cart = cartRepository.findByUsername(username).orElseThrow(() -> {
            log.error(CART_ITEMS_NOT_FOUND_FOR_USER, username);
            return new CartException(EXCEPTION_CART_ITEMS_NOT_FOUND_FOR_USER + username);
        });
        List<String> failedDeletes = productIDs.stream().filter(productID -> {
            try {
                cartItemRepository.deleteProductFromCart(cart, productID);
                log.info(CART_ITEMS_DELETED_FROM_CART, productID);
                return false; // Successful delete
            } catch (Exception e) {
                log.error(UNABLE_TO_DELETE_CART_ITEM_FROM_CART, productID, e.getMessage());
                return true; // Failed delete
            }
        }).collect(Collectors.toList());
        if (!failedDeletes.isEmpty()) {
            log.error(FAILED_TO_DELETE_CART_ITEMS, failedDeletes);
            throw new CartException(EXCEPTION_FAILED_TO_DELETE_ITEMS_FROM_CART + failedDeletes);
        }
        cartServiceHelper.updateCartAmount(cart);
        cartRepository.updateByLastUpdateTime(Timestamp.from(Instant.now()), username);
        String key = "cart" + username;
        redisService.deleteData(key);
        redisService.deleteData("cartList");
        log.info(UPDATED_TOTAL_AMOUNT_CART);
        return getCartItems(username);
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
            log.error(CART_ITEMS_NOT_FOUND_FOR_USER, username);
            return new CartException(EXCEPTION_CART_ITEMS_NOT_FOUND_FOR_USER + username);
        });
        try {
            cartItemRepository.deleteByCart(cart);
            cartRepository.delete(cart);
            String key = "cart" + username;
            redisService.deleteData(key);
            redisService.deleteData("cartList");
            log.info(ITEMS_DELETED_SUCCESSFULLY);
        } catch (Exception exception) {
            log.error(FAILED_TO_DELETE_CART_ITEMS, exception.getMessage());
            throw new CartException(exception.getMessage());
        }
        return ITEMS_DELETED_SUCCESSFULLY;
    }

    /**
     * This method will return the total amount of the cart
     *
     * @param username
     * @return
     */
    @Override
    public double getTotalCartAmount(String username) {
        String key = "cart" + username;
        CartResponse cartResponse = redisService.getData(key, CartResponse.class);
        if (cartResponse != null) {
            return cartResponse.getCartItemsList().stream().mapToDouble(CartItemResponse::getTotalPrice).sum();
        }
        Cart cart = cartRepository.findByUsername(username).orElseThrow(() -> {
            log.error(CART_ITEMS_NOT_FOUND_FOR_USER, username);
            return new CartException(EXCEPTION_CART_ITEMS_NOT_FOUND_FOR_USER + username);
        });
        cartResponse = cartMapper.toCartResponse(cart);
        redisService.setData(key, cartResponse, 300L);
        return cartResponse.getCartItemsList().stream().mapToDouble(CartItemResponse::getTotalPrice).sum();
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
            log.error(CART_ITEMS_NOT_FOUND_FOR_USER, cartRequest.getUsername());
            return new CartException(EXCEPTION_CART_ITEMS_NOT_FOUND_FOR_USER + cartRequest.getUsername());
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
        String key = "cart" + cartRequest.getUsername();
        redisService.deleteData("cartList");
        redisService.deleteData(key);
        return getCartItems(cartRequest.getUsername());
    }

}
