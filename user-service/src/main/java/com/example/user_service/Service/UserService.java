package com.example.user_service.Service;

import com.example.user_service.DTO.Request.UserRequest;
import com.example.user_service.DTO.Response.UserResponse;


public interface UserService {
    UserResponse getUserDetails(String username);

    UserResponse updateUserDetails(UserRequest request);

    UserResponse updatePassword(UserRequest request);
}
