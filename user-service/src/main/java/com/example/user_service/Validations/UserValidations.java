package com.example.user_service.Validations;

import com.example.user_service.DTO.Request.UserRequest;
import com.example.user_service.Exceptions.UserException;

import static com.example.user_service.messages.UserMessages.*;

public class UserValidations {

    /***
     * This method is used to validate the user details
     * @param request
     */
    public static void validateUserDetails(UserRequest request) {
        if (request.getFirstName() == null || request.getFirstName().isEmpty()) {
            throw new UserException(FIRST_NAME_NOT_EMPTY);
        }

        if (request.getLastName() == null || request.getLastName().isEmpty()) {
            throw new UserException(LAST_NAME_NOT_EMPTY);
        }

        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            throw new UserException(EMAIL_NOT_EMPTY);
        }

        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new UserException(PASSWORD_NOT_EMPTY);
        }

        if (request.getPassword().length() < 7) {
            throw new UserException(INVALID_PASSWORD_LENGTH);
        }

        if (request.getContactNumber() == null || request.getContactNumber().isEmpty()) {
            throw new UserException(CONTACT_NOT_EMPTY);
        }

        if (request.getContactNumber().length() != 10 || !(request.getContactNumber().startsWith("6") || request.getContactNumber().startsWith("7") || request.getContactNumber().startsWith("8") || request.getContactNumber().startsWith("9"))) {
            throw new UserException(INVALID_CONTACT_NUMBER);
        }
    }
}
