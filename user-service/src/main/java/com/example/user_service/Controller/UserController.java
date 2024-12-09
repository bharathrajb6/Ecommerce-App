package com.example.user_service.Controller;

import com.example.user_service.DTO.Request.OrderRequest;
import com.example.user_service.DTO.Request.UserRequest;
import com.example.user_service.DTO.Response.ApiResponse;
import com.example.user_service.DTO.Response.OrderResponse;
import com.example.user_service.DTO.Response.Product.ProductResponse;
import com.example.user_service.DTO.Response.UserResponse;
import com.example.user_service.Service.Impl.OrderServiceImpl;
import com.example.user_service.Service.Impl.ProductServiceImpl;
import com.example.user_service.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private ProductServiceImpl productService;
    @Autowired
    private OrderServiceImpl orderService;

    /***
     * This method is used to get the user details
     * @param authentication
     * @return
     */
    @ApiIgnore
    @RequestMapping(value = "/getUserDetails", method = RequestMethod.GET)
    public ApiResponse<UserResponse> getUserDetails(Authentication authentication) {
        return ApiResponse.<UserResponse>builder().result(userService.getUserDetails(authentication.getName())).build();
    }

    /***
     * This method is used to update the user details
     * @param request
     * @return
     */
    @ApiIgnore
    @RequestMapping(value = "/updateDetails", method = RequestMethod.PUT)
    public ApiResponse<UserResponse> updateUserDetails(@RequestBody UserRequest request) {
        return ApiResponse.<UserResponse>builder().result(userService.updateUserDetails(request)).build();
    }

    /***
     * This method is used to update the password
     * @param request
     * @return
     */
    @ApiIgnore
    @RequestMapping(value = "/updatePassword", method = RequestMethod.PUT)
    public ApiResponse<UserResponse> updatePassword(@RequestBody UserRequest request) {
        return ApiResponse.<UserResponse>builder().result(userService.updatePassword(request)).build();
    }

    /***
     * This method is used to get all the products
     * @return
     */
    @ApiIgnore
    @RequestMapping(value = "/product", method = RequestMethod.GET)
    public ApiResponse<List<ProductResponse>> getProducts() {
        return ApiResponse.<List<ProductResponse>>builder().result(productService.getAllProducts()).build();
    }

    /***
     * This method is used to get the product details by product id
     * @param prodID
     * @return
     */
    @Operation(summary = "Get Product", description = "Get the product from catalog based on product ID")
    @RequestMapping(value = "/product/{prodID}", method = RequestMethod.GET)
    public ApiResponse<ProductResponse> getProduct(@PathVariable String prodID) {
        return ApiResponse.<ProductResponse>builder().result(productService.getProduct(prodID)).build();
    }

    /***
     * This method is used to search the products
     * @param criteria
     * @return
     */
    @Operation(summary = "Search Product", description = "Search product in catalog based on ID, name")
    @RequestMapping(value = "/product/search/{criteria}", method = RequestMethod.GET)
    public ApiResponse<List<ProductResponse>> searchProducts(@PathVariable String criteria) {
        return ApiResponse.<List<ProductResponse>>builder().result(productService.searchProduct(criteria)).build();
    }


    /**
     * This method is used to place the order
     *
     * @param request
     * @return
     */
    @Operation(summary = "Place Order", description = "Create order")
    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public OrderResponse placeOrder(@RequestBody OrderRequest request) {
        return orderService.placeOrder(request);
    }

    /**
     * This method is used to get the order details based on order ID
     *
     * @param orderID
     * @return
     */
    @Operation(summary = "Get Order Details", description = "Get the order details based on order ID")
    @RequestMapping(value = "/order/{orderID}", method = RequestMethod.GET)
    public OrderResponse getOrderDetails(@PathVariable String orderID) {
        return orderService.getOrderDetails(orderID);
    }

    /**
     * This method is used to cancel the order based on order ID
     *
     * @param orderID
     * @return
     */
    @Operation(summary = "Cancel Order", description = "Cancel the order based on orderID")
    @RequestMapping(value = "/order/cancel/{orderID}", method = RequestMethod.PUT)
    public String cancelOrder(@PathVariable String orderID) {
        return orderService.cancelOrder(orderID);
    }

    /**
     * This method is used to get all cancelled orders
     *
     * @return
     */
    @Operation(summary = "Get All Cancelled Orders", description = "Get all cancelled orders based on username")
    @RequestMapping(value = "/order/cancelled", method = RequestMethod.GET)
    public List<OrderResponse> getAllCancelledOrders() {
        return orderService.getAllCancelledOrders();
    }


    /**
     * This method is used to track the order based on tracking number
     *
     * @param trackingNumber
     * @return
     */
    @Operation(summary = "Track Order", description = "Track order based on tracking number")
    @RequestMapping(value = "/order/track/{trackingNumber}", method = RequestMethod.GET)
    public OrderResponse getOrderByTrackingNumber(@PathVariable String trackingNumber) {
        return orderService.getOrderByTrackingNumber(trackingNumber);
    }

    /**
     * This method is used to get the orders based on username
     *
     * @return
     */
    @RequestMapping(value = "/order/search", method = RequestMethod.GET)
    public List<OrderResponse> getOrderByUsername() {
        return orderService.getAllOrdersByUserName();
    }

}
