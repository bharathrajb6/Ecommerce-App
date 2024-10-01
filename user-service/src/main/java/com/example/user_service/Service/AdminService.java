package com.example.user_service.Service;

import com.example.user_service.DTO.Response.UserResponse;

public interface AdminService {

    UserResponse getAdminDetails(String username);
}
