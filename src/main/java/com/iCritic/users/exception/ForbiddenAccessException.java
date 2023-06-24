package com.iCritic.users.exception;

public class ForbiddenAccessException extends RuntimeException {
    public ForbiddenAccessException() {
        super("Insufficient access rights");
    }

    public ForbiddenAccessException(String message) {
        super(message);
    }
}
