package com.bankflow.exceptions;

public class DublicateDataException extends RuntimeException{
    public DublicateDataException(String message) {
        super(message);
    }

    public DublicateDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
