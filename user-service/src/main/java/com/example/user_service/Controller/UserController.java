package com.example.user_service.Controller;

import com.example.user_service.DTO.Request.UserRequest;
import com.example.user_service.DTO.Response.ApiResponse;
import com.example.user_service.DTO.Response.UserResponse;
import com.example.user_service.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    // This is a placeholder for the UserController class
    // This class will be implemented in the next step
    @Autowired
    UserService userService;

    @RequestMapping(method = RequestMethod.POST)
    public ApiResponse<UserResponse> createUser(@RequestBody UserRequest request) {
        return ApiResponse.<UserResponse>builder().result(userService.createUser(request)).build();
    }


    @RequestMapping(method = RequestMethod.GET)
    public ApiResponse<UserResponse> getUser(@RequestBody UserRequest request){
        return ApiResponse.<UserResponse>builder().result(userService.getUserDetails(request)).build();
    }
}
