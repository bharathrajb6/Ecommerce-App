package com.example.user_service.Controller;

import com.example.user_service.DTO.Request.UserRequest;
import com.example.user_service.DTO.Response.ApiResponse;
import com.example.user_service.DTO.Response.ProductResponse;
import com.example.user_service.DTO.Response.UserResponse;
import com.example.user_service.Service.Impl.ProductServiceImpl;
import com.example.user_service.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    ProductServiceImpl productService;

    /***
     * This method is used to get the user details
     * @param authentication
     * @return
     */
    @RequestMapping(value = "getUserDetails", method = RequestMethod.GET)
    public ApiResponse<UserResponse> getUserDetails(Authentication authentication) {
        return ApiResponse.<UserResponse>builder().result(userService.getUserDetails(authentication.getName())).build();
    }

    /***
     * This method is used to update the user details
     * @param request
     * @return
     */
    @RequestMapping(value = "updateDetails", method = RequestMethod.PUT)
    public ApiResponse<UserResponse> updateUserDetails(@RequestBody UserRequest request) {
        return ApiResponse.<UserResponse>builder().result(userService.updateUserDetails(request)).build();
    }

    /***
     * This method is used to update the password
     * @param request
     * @return
     */
    @RequestMapping(value = "updatePassword", method = RequestMethod.PUT)
    public ApiResponse<UserResponse> updatePassword(@RequestBody UserRequest request) {
        return ApiResponse.<UserResponse>builder().result(userService.updatePassword(request)).build();
    }

    /***
     * This method is used to get all the products
     * @return
     */
    @RequestMapping(value = "/product", method = RequestMethod.GET)
    public ApiResponse<List<ProductResponse>> getProducts() {
        return ApiResponse.<List<ProductResponse>>builder().result(productService.getAllProducts()).build();
    }

    /***
     * This method is used to get the product details by product id
     * @param prodID
     * @return
     */
    @RequestMapping(value = "/product/{prodID}", method = RequestMethod.GET)
    public ApiResponse<ProductResponse> getProduct(@PathVariable String prodID) {
        return ApiResponse.<ProductResponse>builder().result(productService.getProduct(prodID)).build();
    }

    /***
     * This method is used to search the products
     * @param criteria
     * @return
     */
    @RequestMapping(value = "/product/search/{criteria}",method = RequestMethod.GET)
    public ApiResponse<List<ProductResponse>> searchProducts(@PathVariable String criteria){
        return ApiResponse.<List<ProductResponse>>builder().result(productService.searchProduct(criteria)).build();
    }

}
