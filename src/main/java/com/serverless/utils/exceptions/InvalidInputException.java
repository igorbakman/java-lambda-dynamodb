package com.serverless.utils.exceptions;

public class InvalidInputException extends RuntimeException {

    private final String message;

    public InvalidInputException(String msg, Exception e) {
        super(msg, e);
        this.message = msg;
    }

    public String getMessage() {
        return this.message;
    }
}
