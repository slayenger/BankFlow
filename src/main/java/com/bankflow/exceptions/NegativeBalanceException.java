package com.bankflow.exceptions;

public class NegativeBalanceException extends RuntimeException{
    public NegativeBalanceException(String message) {
        super(message);
    }

    public NegativeBalanceException(String message, Throwable cause) {
        super(message, cause);
    }
}
