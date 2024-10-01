package com.example.user_service.Service;

import com.example.user_service.DTO.Request.UserRequest;
import com.example.user_service.DTO.Response.UserResponse;

public interface AuthenticationService {
    String register(UserRequest request);

    UserResponse getUserDetails(UserRequest request);

    String login(UserRequest request);
}
