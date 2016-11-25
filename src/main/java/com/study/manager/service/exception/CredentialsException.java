package com.study.manager.service.exception;

public class CredentialsException extends ServiceException {

    public CredentialsException(ErrorCode errorCode) {
        super(errorCode);
    }
}
