package com.bankflow.exceptions;

public class InvalidTransactionOperationException extends RuntimeException {
    public InvalidTransactionOperationException(String message) {
        super(message);
    }

    public InvalidTransactionOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
