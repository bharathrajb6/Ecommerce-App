package com.example.user_service.Service.Impl;

import com.example.user_service.DTO.Request.UserRequest;
import com.example.user_service.DTO.Response.UserResponse;
import com.example.user_service.Exceptions.UserException;
import com.example.user_service.Mapper.UserMapper;
import com.example.user_service.Model.User;
import com.example.user_service.Repository.UserRepository;
import com.example.user_service.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.user_service.Validations.UserValidations.validateUserDetails;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    /***
     * This method is used to get the user details based on username
     * @param username
     * @return
     */
    @Override
    public UserResponse getUserDetails(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> {
            logger.error("User not found");
            return new UserException("User not found");
        });
        return userMapper.toUserResponse(user);
    }

    /***
     * This method is used to update the user details
     * @param request
     * @return
     */
    @Override
    public UserResponse updateUserDetails(UserRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            validateUserDetails(request);
            User user = userMapper.toUser(request);
            userRepository.updateUserDetails(user.getFirstName(), user.getLastName(), user.getEmail(), user.getContactNumber(), user.getUsername());
            return getUserDetails(user.getUsername());
        } else {
            throw new UserException("User not found");
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
            userRepository.updatePassword(request.getUsername(), passwordEncoder.encode(request.getPassword()));
            return getUserDetails(request.getUsername());
        } else {
            throw new UserException("User not found");
        }
    }
}
