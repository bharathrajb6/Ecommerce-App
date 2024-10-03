package com.example.user_service.Controller;

import com.example.user_service.DTO.Request.ProductRequest;
import com.example.user_service.DTO.Request.UserRequest;
import com.example.user_service.DTO.Response.ApiResponse;
import com.example.user_service.DTO.Response.ProductResponse;
import com.example.user_service.DTO.Response.UserResponse;
import com.example.user_service.Service.Impl.ProductServiceImpl;
import com.example.user_service.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/admin/")
public class AdminController {

    @Autowired
    private UserService userService;

    private ProductServiceImpl productService;

    @RequestMapping(value = "getDetails", method = RequestMethod.GET)
    public ApiResponse<UserResponse> getUserDetails(Authentication authentication) {
        return ApiResponse.<UserResponse>builder().result(userService.getUserDetails(authentication.getName())).build();
    }

    @RequestMapping(value = "updateDetails", method = RequestMethod.PUT)
    public ApiResponse<UserResponse> updateDetails(@RequestBody UserRequest request) {
        return ApiResponse.<UserResponse>builder().result(userService.updateUserDetails(request)).build();
    }

    @RequestMapping(value = "updatePassword", method = RequestMethod.PUT)
    public ApiResponse<UserResponse> updatePassword(@RequestBody UserRequest request) {
        return ApiResponse.<UserResponse>builder().result(userService.updatePassword(request)).build();
    }

    @RequestMapping(value = "/product", method = RequestMethod.POST)
    public ApiResponse<ProductResponse> addProduct(@RequestBody ProductRequest request) {
        return ApiResponse.<ProductResponse>builder().result(productService.addProduct(request)).build();
    }

    @RequestMapping(value = "/product/{prodID}", method = RequestMethod.PUT)
    public ApiResponse<ProductResponse> updateProduct(@PathVariable String prodID, @RequestBody ProductRequest request) {
        return ApiResponse.<ProductResponse>builder().result(productService.updateProduct(prodID, request)).build();
    }


}
