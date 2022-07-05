package com.revature.courseapp.exceptions;

public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException (String errorMessage) {
        super(errorMessage);
    }
}
