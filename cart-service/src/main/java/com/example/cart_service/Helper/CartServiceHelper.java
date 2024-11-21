package com.example.cart_service.Helper;

import com.example.cart_service.DTO.Request.CartItemsRequest;
import com.example.cart_service.DTO.Request.CartRequest;
import com.example.cart_service.DTO.Response.ProductResponse;
import com.example.cart_service.Exception.CartException;
import com.example.cart_service.Mapper.CartMapper;
import com.example.cart_service.Model.Cart;
import com.example.cart_service.Model.CartItems;
import com.example.cart_service.Repository.CartItemRepository;
import com.example.cart_service.Repository.CartRepository;
import com.example.cart_service.Service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CartServiceHelper {

    @Autowired
    private ProductService productService;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    /**
     * This method will create the cart object for the new user
     * @param cartRequest
     * @return
     */
    public Cart addItemsToCart(CartRequest cartRequest) {
        if (!checkIfProductsAreFound(cartRequest.getCartItemsList())) {
            return null;
        }

        Cart cart = cartMapper.toCart(cartRequest);
        String cartID = UUID.randomUUID().toString();
        Timestamp currentTimeStamp = Timestamp.from(Instant.now());

        cart.setUsername(cartRequest.getUsername());
        cart.setCartID(cartID);
        cart.setCreatedAt(currentTimeStamp);
        cart.setUpdatedAt(currentTimeStamp);
        List<CartItems> cartItems = new ArrayList<>();
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
        cart.setTotalAmount(calculateTotalAmount(cart.getCartItemsList()));

        return cart;
    }

    /**
     * This method will check the product stock for the current request
     *
     * @param productRequestList
     * @return
     */
    private boolean checkIfProductsAreFound(List<CartItemsRequest> productRequestList) {
        return productRequestList.stream().allMatch(productRequest -> productRequest.getQuantity() <= productService.getStock(productRequest.getProdID()));
    }

    /**
     * This method will return the total amount for the list of cart items
     *
     * @param cartItems
     * @return
     */
    public double calculateTotalAmount(List<CartItems> cartItems) {
        return cartItems.stream().mapToDouble(CartItems::getTotalPrice).sum();
    }

    /**
     * This method will update the total cart amount after adding the items to cart
     * @param cart
     */
    public void updateCartAmount(Cart cart) {
        List<CartItems> cartItems = cartItemRepository.findByCart(cart);
        if (cartItems.isEmpty()) {
            return;
        }
        double updatedTotalAmount = calculateTotalAmount(cartItems);
        try {
            cartRepository.updateTotalAmount(updatedTotalAmount, cart.getCartID());
        } catch (Exception exception) {
            throw new CartException(exception.getMessage());
        }
    }

    /**
     * This method separate the products into existing products and new products
     * @param cart
     * @param cartRequest
     * @return
     */
    public Map<String, List<String>> getExistingAndNewProductFromRequest(Cart cart, CartRequest cartRequest) {

        // Extract product IDs from the existing cart
        Set<String> existingCartItems = cart.getCartItemsList().stream().map(CartItems::getProdID).collect(Collectors.toSet());
        // Extract product IDs from the new cart request
        Set<String> newCartItems = cartRequest.getCartItemsList().stream().map(CartItemsRequest::getProdID).collect(Collectors.toSet());

        List<String> sameProductIDs = existingCartItems.stream().filter(newCartItems::contains).toList();
        List<String> differentProductIDs = existingCartItems.stream().filter(prodID -> !newCartItems.contains(prodID)).toList();

        Map<String, List<String>> result = new HashMap<>();
        result.put("sameProductIDs", sameProductIDs);
        result.put("differentProductIDs", differentProductIDs);
        return result;
    }

    /**
     * This method will add the products into cartItems DB.If products are already there then it will update thw quantity.If the products are not there then it will create a new entry.
     * @param productIDs
     * @param cartRequest
     * @param cart
     * @param type
     */
    public void addProductsToCart(List<String> productIDs, CartRequest cartRequest, Cart cart, String type) {
        if (type.equals("SAME")) {
            for (String productID : productIDs) {
                Optional<CartItemsRequest> cartItemsRequest = cartRequest.getCartItemsList().stream().filter(prodID -> prodID.equals(productID)).findFirst();
                if (cartItemsRequest.isPresent()) {
                    CartItems cartItems = cartItemRepository.findByProductIDAndCart(cart, productID);
                    int totalQuantity = cartItems.getQuantity() + cartItemsRequest.get().getQuantity();
                    double totalPrice = totalQuantity * cartItems.getPrice();
                    cartItemRepository.updateCartProductQuantity(cartItemsRequest.get().getQuantity(), totalPrice, cartItems.getCartItemID());
                }
            }
        } else if (type.equals("DIFF")) {
            List<CartItems> cartItemList = new ArrayList<>();
            for(String prodID : productIDs){
                Optional<CartItemsRequest> cartItemsRequest = cartRequest.getCartItemsList().stream().filter(productID -> productID.equals(prodID)).findFirst();
                if(cartItemsRequest.isPresent()){
                    CartItems cartItem = new CartItems();
                    cartItem.setCartItemID(UUID.randomUUID().toString());
                    cartItem.setProdID(cartItemsRequest.get().getProdID());
                    cartItem.setQuantity(cartItemsRequest.get().getQuantity());
                    cartItem.setPrice(cartItemsRequest.get().getPrice());
                    cartItem.setTotalPrice(cartItem.getPrice() * cartItem.getQuantity());
                    cartItem.setCart(cart);
                    cartItemList.add(cartItem);
                }
            }
            try {
                cartItemRepository.saveAll(cartItemList);
                updateCartAmount(cart);
                cartRepository.updateByLastUpdateTime(Timestamp.from(Instant.now()), cartRequest.getUsername());
                log.info("Items are added successfully to cart");
            } catch (Exception exception) {
                log.error("Unable to save the items to cart - {}", exception.getMessage());
                throw new CartException(exception.getMessage());
            }
        }
    }

}
