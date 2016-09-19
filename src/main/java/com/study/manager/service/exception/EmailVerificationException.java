package com.study.manager.service.exception;

public class EmailVerificationException extends RuntimeException {

    public EmailVerificationException(final String message) {
        super(message);
    }
}
