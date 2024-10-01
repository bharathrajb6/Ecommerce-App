package com.example.user_service.Validations;

import com.example.user_service.DTO.Request.UserRequest;
import com.example.user_service.Exceptions.UserException;

public class UserValidations {

    public static void validateUserDetails(UserRequest request) {
        if (request.getFirstName() == null || request.getFirstName().isEmpty()) {
            throw new UserException("First name cannot be empty.");
        }

        if (request.getLastName() == null || request.getLastName().isEmpty()) {
            throw new UserException("Last name cannot be empty.");
        }

        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            throw new UserException("Email cannot be empty.");
        }

        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new UserException("Password cannot be empty.");
        }

        if (request.getPassword().length() < 7) {
            throw new UserException("Password length should be 7 or more");
        }

        if (request.getContactNumber() == null || request.getContactNumber().isEmpty()) {
            throw new UserException("Contact number cannot be empty.");
        }

        if (request.getContactNumber().length() != 10 || !(request.getContactNumber().startsWith("6") || request.getContactNumber().startsWith("7") || request.getContactNumber().startsWith("8") || request.getContactNumber().startsWith("9"))) {
            throw new UserException("Contact number is not valid.");
        }
    }
}
