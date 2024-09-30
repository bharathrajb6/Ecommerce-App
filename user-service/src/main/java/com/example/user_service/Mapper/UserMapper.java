package com.example.user_service.Mapper;

import com.example.user_service.DTO.Request.UserRequest;
import com.example.user_service.DTO.Response.UserResponse;
import com.example.user_service.Model.User;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRequest userRequest);
    UserResponse toUserResponse(User user);
}
