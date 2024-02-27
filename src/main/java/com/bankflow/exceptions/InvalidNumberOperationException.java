package com.bankflow.exceptions;

public class InvalidNumberOperationException extends RuntimeException {
    public InvalidNumberOperationException(String message) {
        super(message);
    }

    public InvalidNumberOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
