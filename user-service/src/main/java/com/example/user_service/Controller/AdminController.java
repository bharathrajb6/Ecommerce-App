package com.example.user_service.Controller;

import com.example.user_service.DTO.Request.ProductRequest;
import com.example.user_service.DTO.Request.UserRequest;
import com.example.user_service.DTO.Response.ApiResponse;
import com.example.user_service.DTO.Response.ProductResponse;
import com.example.user_service.DTO.Response.UserResponse;
import com.example.user_service.Service.Impl.ProductServiceImpl;
import com.example.user_service.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;


@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
@ApiIgnore
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductServiceImpl productService;

    /***
     * This method is used to get the details of the admin
     * @param authentication
     * @return
     */
    @RequestMapping(value = "/getDetails", method = RequestMethod.GET)
    public ApiResponse<UserResponse> getUserDetails(Authentication authentication) {
        return ApiResponse.<UserResponse>builder().result(userService.getUserDetails(authentication.getName())).build();
    }

    /***
     * This method is used to update the details of the admin
     * @param request
     * @return
     */
    @RequestMapping(value = "/updateDetails", method = RequestMethod.PUT)
    public ApiResponse<UserResponse> updateDetails(@RequestBody UserRequest request) {
        return ApiResponse.<UserResponse>builder().result(userService.updateUserDetails(request)).build();
    }

    /***
     * This method is used to update the password of the user
     * @param request
     * @return
     */
    @RequestMapping(value = "/updatePassword", method = RequestMethod.PUT)
    public ApiResponse<UserResponse> updatePassword(@RequestBody UserRequest request) {
        return ApiResponse.<UserResponse>builder().result(userService.updatePassword(request)).build();
    }

    /***
     * This method is used to add a product
     * @param request
     * @return
     */
    @RequestMapping(value = "/product", method = RequestMethod.POST)
    public ApiResponse<ProductResponse> addProduct(@RequestBody ProductRequest request) {
        return ApiResponse.<ProductResponse>builder().result(productService.addProduct(request)).build();
    }

    /***
     * This method is used to update the product
     * @param prodID
     * @param request
     * @return
     */
    @RequestMapping(value = "/product/{prodID}", method = RequestMethod.PUT)
    public ApiResponse<ProductResponse> updateProduct(@PathVariable String prodID, @RequestBody ProductRequest request) {
        return ApiResponse.<ProductResponse>builder().result(productService.updateProduct(prodID, request)).build();
    }
}
