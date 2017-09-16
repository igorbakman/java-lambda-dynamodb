package com.serverless.utils.exceptions;

public class DatasourceException extends RuntimeException {

    private final String message;

    public DatasourceException(String msg, Throwable e) {
        super(msg, e);
        this.message = msg;
    }

    public DatasourceException(String msg) {
        message = msg;
    }

    @Override
    public String getMessage() {
        return message;
    }
}