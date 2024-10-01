package com.example.user_service.Service;

import com.example.user_service.DTO.Request.UserRequest;

public interface AuthenticationService {
    String register(UserRequest request);

    String login(UserRequest request);
}
