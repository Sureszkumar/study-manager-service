package com.study.manager.controller;

import java.io.IOException;

import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.study.manager.domain.ServiceResponse;
import com.study.manager.domain.User;
import com.study.manager.service.UserService;
import com.study.manager.service.exception.EmailVerificationException;
import com.study.manager.service.exception.ServiceException;
import com.study.manager.util.EmailService;

@RestController
@RequestMapping(value = "/user")
public class LoginController {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

	@Inject
	private UserService userService;

	@Inject
	private EmailService emailService;

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public ServiceResponse createUser(@RequestBody final User user) {
		LOGGER.debug("Received request to signup the {}", user);
		User createdUser;
		ServiceResponse response = new ServiceResponse();
		try {
			createdUser = userService.create(user);

		} catch (ServiceException e) {
			response.setSuccess(false);
			response.setMessage(e.getMessage());
			response.setErrorCode(e.getErrorCode());
			return response;
		} catch (Exception e) {
			response.setSuccess(false);
			response.setMessage(e.getMessage());
			return response;
		}
		emailService.sendVerifyToken(createdUser.getId(), createdUser.getEmail(), createdUser.getName());
		response.setSuccess(true);
		response.setMessage("User created for email " + user.getEmail() + " Check your mail and verify");
		return response;
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ServiceResponse loginUser(@RequestBody final User user) {
		LOGGER.debug("Received request to create the {}", user);
		ServiceResponse response = new ServiceResponse();
		try {
			User createdUser = userService.loginUser(user);
			response.setSuccess(true);
			response.setMessage("User logged in successfully");
			response.setUserId(createdUser.getId());
			response.setAuthToken(createdUser.getAuthToken());
			return response;
		} catch (ServiceException e) {
			response.setSuccess(false);
			response.setMessage(e.getMessage());
			response.setErrorCode(e.getErrorCode());
			return response;
		} catch (Exception e) {
			response.setSuccess(false);
			response.setMessage(e.getMessage());
			return response;
		}
	}

	@RequestMapping(value = "/sendNewPassword", method = RequestMethod.POST)
	public ServiceResponse sendPassword(@RequestBody final User user) {
		LOGGER.debug("Received request to sendPassword {}", user.getEmail());
		ServiceResponse response = new ServiceResponse();
		try {
			userService.sendPassword(user.getEmail());
			response.setSuccess(true);
			response.setMessage("Password sent to mail successfully");
			return response;
		} catch (ServiceException e) {
			response.setSuccess(false);
			response.setMessage(e.getMessage());
			response.setErrorCode(e.getErrorCode());
			return response;
		} catch (Exception e) {
			response.setSuccess(false);
			response.setMessage(e.getMessage());
			return response;
		}
	}

	@RequestMapping(value = "/verifyEmail", method = RequestMethod.GET)
	public String verifyEmail(@RequestParam("user") final String user,
			@RequestParam("token") final String emailVerifyToken) {
		userService.verifyEmail(user, emailVerifyToken);
		return emailService.getVerifyPasswordContent();
	}

	@ExceptionHandler(EmailVerificationException.class)
	void handleEmailVerfificationException(HttpServletResponse response, EmailVerificationException e)
			throws IOException {
		response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
	}

	@ExceptionHandler(IllegalArgumentException.class)
	void handleBadRequests(HttpServletResponse response) throws IOException {
		response.sendError(HttpStatus.BAD_REQUEST.value(), "Email/Password is invalid");
	}

	@ExceptionHandler(EntityExistsException.class)
	void handleUserExistRequests(HttpServletResponse response) throws IOException {
		response.sendError(HttpStatus.CONFLICT.value(), "Email address already exists");
	}

	@ExceptionHandler(EntityNotFoundException.class)
	void handleUserNotFoundRequests(HttpServletResponse response) throws IOException {
		response.sendError(HttpStatus.NOT_FOUND.value(), "Email address not found. Signup to continue");
	}
}
