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

    @Autowired
    private RedisServiceImpl redisService;

    /***
     * This method is used to get the details of the admin
     * @param username
     * @return
     */
    @Override
    public UserResponse getAdminDetails(String username) {
        UserResponse userResponse = redisService.getData(username, UserResponse.class);
        if (userResponse != null) {
            return userResponse;
        } else {
            User user = userRepository.findByUsername(username).orElseThrow(() -> new UserException(USERNAME_NOT_FOUND));
            userResponse = userMapper.toUserResponse(user);

            // Add the data to cache
            redisService.setData(username, userResponse, 300L);

            log.info(ADMIN_DATA_RETRIVED);
            return userResponse;
        }
    }
}
