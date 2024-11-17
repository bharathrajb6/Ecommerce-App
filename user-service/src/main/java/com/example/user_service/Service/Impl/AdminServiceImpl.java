package com.example.user_service.Service.Impl;

import com.example.user_service.DTO.Response.UserResponse;
import com.example.user_service.Exceptions.UserException;
import com.example.user_service.Mapper.UserMapper;
import com.example.user_service.Model.User;
import com.example.user_service.Repository.UserRepository;
import com.example.user_service.Service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.user_service.messages.UserMessages.ADMIN_DATA_RETRIVED;
import static com.example.user_service.messages.UserMessages.USERNAME_NOT_FOUND;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    /***
     * This method is used to get the details of the admin
     * @param username
     * @return
     */
    @Override
    public UserResponse getAdminDetails(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> {
            return new UserException(USERNAME_NOT_FOUND);
        });
        log.info(ADMIN_DATA_RETRIVED);
        return userMapper.toUserResponse(user);
    }
}
