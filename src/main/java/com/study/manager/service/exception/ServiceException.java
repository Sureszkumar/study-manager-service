package com.study.manager.service.exception;

public class ServiceException extends RuntimeException {

	private String errorCode;
	
    public String getErrorCode() {
		return errorCode;
	}

	public ServiceException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode.name();
    }
}
