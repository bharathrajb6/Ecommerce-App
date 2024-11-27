package com.example.user_service.Controller;

import com.example.user_service.DTO.Request.UserRequest;
import com.example.user_service.DTO.Response.ApiResponse;
import com.example.user_service.Service.Impl.AuthenticationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@ApiIgnore
public class AuthenticationController {
    // This is a placeholder for the AuthenticationController class
    // This class will be implemented in the next step
    @Autowired
    AuthenticationServiceImpl authenticationService;

    /***
     * Register a new user
     * @param request
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ApiResponse<String> register(@RequestBody UserRequest request) {
        return ApiResponse.<String>builder().result(authenticationService.register(request)).build();
    }

    /***
     * Login a user
     * @param request
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ApiResponse<String> login(@RequestBody UserRequest request) {
        return ApiResponse.<String>builder().result(authenticationService.login(request)).build();
    }
}
