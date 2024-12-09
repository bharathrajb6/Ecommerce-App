package com.example.user_service.Service.Impl;

import com.example.user_service.DTO.Request.UserRequest;
import com.example.user_service.DTO.Response.UserResponse;
import com.example.user_service.Exceptions.UserException;
import com.example.user_service.Mapper.UserMapper;
import com.example.user_service.Model.User;
import com.example.user_service.Repository.UserRepository;
import com.example.user_service.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.user_service.Validations.UserValidations.validateUserDetails;
import static com.example.user_service.messages.UserMessages.*;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisServiceImpl redisService;


    /***
     * This method is used to get the user details based on username
     * @param username
     * @return
     */
    @Override
    public UserResponse getUserDetails(String username) {
        UserResponse userResponse = redisService.getData(username, UserResponse.class);
        if (userResponse != null) {
            return userResponse;
        } else {
            User user = userRepository.findByUsername(username).orElseThrow(() -> {
                log.error(USERNAME_NOT_FOUND);
                return new UserException(USERNAME_NOT_FOUND);
            });
            userResponse = userMapper.toUserResponse(user);
            // Add the data to cache
            redisService.setData(username, userResponse, 300L);
            return userResponse;
        }
    }

    /***
     * This method is used to update the user details
     * @param request
     * @return
     */
    @Override
    public UserResponse updateUserDetails(UserRequest request) {
        // Check if username is existed
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            // Validate the user details
            validateUserDetails(request);

            User user = userMapper.toUser(request);

            // Update the user details
            userRepository.updateUserDetails(user.getFirstName(), user.getLastName(), user.getEmail(), user.getContactNumber(), user.getUsername());

            // Delete the old user data from cache
            redisService.deleteData(user.getUsername());

            log.info(USER_DETAILS_UPDATED);
            return getUserDetails(user.getUsername());
        } else {
            log.error(USERNAME_NOT_FOUND);
            throw new UserException(USERNAME_NOT_FOUND);
        }
    }

    /***
     * This method is used to update the password
     * @param request
     * @return
     */
    @Override
    public UserResponse updatePassword(UserRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            // Update the user password in database
            userRepository.updatePassword(request.getUsername(), passwordEncoder.encode(request.getPassword()));

            // Delete the old user data from cache
            redisService.deleteData(request.getUsername());

            // Log the operation
            log.info(PASSWORD_UPDATED);
            return getUserDetails(request.getUsername());
        } else {
            throw new UserException(USERNAME_NOT_FOUND);
        }
    }
}
