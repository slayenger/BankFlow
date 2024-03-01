package com.bankflow.exceptions;

public class UserDataChangeException extends RuntimeException{
    public UserDataChangeException(String message) {
        super(message);
    }

    public UserDataChangeException(String message, Throwable cause) {
        super(message, cause);
    }
}
