package com.revature.courseapp.exceptions;

public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException () {
        super();
    }

    public UserAlreadyExistsException (String errorMessage) {
        super(errorMessage);
    }
}
