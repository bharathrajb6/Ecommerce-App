package com.example.user_service.Exception;


public class UserException extends RuntimeException {

    public UserException(String message){
        super(message);
    }

    public UserException(String message,Throwable throwable){
        super(message,throwable);
    }

}
