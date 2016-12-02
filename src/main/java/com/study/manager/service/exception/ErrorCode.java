package com.study.manager.service.exception;

public enum ErrorCode {

	SM_100("User not subscribed"),
	SM_101("No books included in custom course"),
	SM_102("Book not found"),
	SM_103("User not exist"),
	SM_104("Entity not exist"),
	SM_105("CourseProficiency already exist"),
	SM_106("Invalid email id"),
	SM_107("Exception while finding user"),
	SM_108("User not found for email"),
	SM_109("Email not verified"),
	SM_110("Invalid password"),
	SM_111("Invalid email verify token for user"),
	SM_112("User not verified yet"),
	SM_113("Exception while encrypting password"),
	SM_114("User already subscribed to this course"),
	SM_115("Course not found"),
	SM_116("Email already exists"),
	SM_117("No of pages is higher than pages unread");

	private String message;

	ErrorCode(String message) {
		this.message = message;
	}
	
	public String getMessage(){
		return message;
	}
}
