package com.bankflow.exceptions;

public class InvalidEmailOperationException extends RuntimeException{
    public InvalidEmailOperationException(String message) {
        super(message);
    }

    public InvalidEmailOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
