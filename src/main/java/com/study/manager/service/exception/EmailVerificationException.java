package com.study.manager.service.exception;

public class EmailVerificationException extends ServiceException {

    public EmailVerificationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
