package com.serverless.api;

public enum ResponseStatus {

    OK(200),
    BAD_REQUEST(400),
    SERVER_ERROR(500);

    private final int status;

    ResponseStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
