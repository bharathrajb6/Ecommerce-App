package com.example.user_service.Service.Impl;

import com.example.user_service.DTO.Request.UserRequest;
import com.example.user_service.DTO.Response.UserResponse;
import com.example.user_service.Exceptions.UserException;
import com.example.user_service.Mapper.UserMapper;
import com.example.user_service.Model.Token;
import com.example.user_service.Model.User;
import com.example.user_service.Repository.TokenRepository;
import com.example.user_service.Repository.UserRepository;
import com.example.user_service.Service.JwtService;
import com.example.user_service.Service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenRepository tokenRepository;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    public String register(UserRequest request) {
        if (userRepository.findById(request.getUsername()).isPresent()) {
            logger.error("Username is already exists");
            throw new UserException("User already exists");
        }
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        saveUserToken(jwtToken, user);
        return jwtToken;
    }

    public UserResponse getUserDetails(UserRequest request) {
        User user = userRepository.findById(request.getUsername()).orElseThrow(() -> {
            logger.error("User not found.");
            return new UserException("User not found.");
        });
        return userMapper.toUserResponse(user);
    }

    @Override
    public String login(UserRequest request) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (Exception e) {
            authentication = null;
        }
        User user = userRepository.findById(request.getUsername()).orElse(null);
        if (user != null && authentication != null) {
            String jwt_token = jwtService.generateToken(user);
            revokeAllTokensByUser(user);
            saveUserToken(jwt_token, user);
            return jwt_token;
        } else {
            return null;
        }
    }

    private void saveUserToken(String jwt_token, User user) {
        Token token = new Token();
        token.setTokenId(String.valueOf(UUID.randomUUID()));
        token.setToken(jwt_token);
        token.setUser(user);
        token.setLoggedOut(false);
        tokenRepository.save(token);
    }

    private void revokeAllTokensByUser(User user) {
        List<Token> validTokensListByUser = tokenRepository.findAllTokens(user.getUsername());
        if (!validTokensListByUser.isEmpty()) {
            validTokensListByUser.forEach(t -> {
                t.setLoggedOut(true);
            });
        }
        tokenRepository.deleteAll(validTokensListByUser);
    }
}
