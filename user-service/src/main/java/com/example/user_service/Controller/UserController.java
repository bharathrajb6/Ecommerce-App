package com.example.user_service.Controller;

import com.example.user_service.DTO.Request.UserRequest;
import com.example.user_service.DTO.Response.ApiResponse;
import com.example.user_service.DTO.Response.UserResponse;
import com.example.user_service.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "getUserDetails", method = RequestMethod.GET)
    public ApiResponse<UserResponse> getUserDetails(Authentication authentication) {
        return ApiResponse.<UserResponse>builder().result(userService.getUserDetails(authentication.getName())).build();
    }

    @RequestMapping(value = "updateDetails", method = RequestMethod.PUT)
    public ApiResponse<UserResponse> updateUserDetails(@RequestBody UserRequest request) {
        return ApiResponse.<UserResponse>builder().result(userService.updateUserDetails(request)).build();
    }

    @RequestMapping(value = "updatePassword", method = RequestMethod.PUT)
    public ApiResponse<UserResponse> updatePassword(@RequestBody UserRequest request) {
        return ApiResponse.<UserResponse>builder().result(userService.updatePassword(request)).build();
    }

}
