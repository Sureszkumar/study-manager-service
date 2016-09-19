package com.study.manager.controller;

import java.io.IOException;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.study.manager.domain.ServiceResponse;
import com.study.manager.service.UserService;
import com.study.manager.service.exception.EmailVerificationException;

@RestController
@RequestMapping(value = "/admin")
public class AdminController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

	@Inject
	private UserService userService;


	/*----------------User services -------------------*/

	@RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
	public ServiceResponse delete(@PathVariable("id") final Long id) {
		LOGGER.debug("Received request to retrieve user : {}", id);
		ServiceResponse response = new ServiceResponse();
		try {
			userService.delete(id);
			response.setSuccess(true);
			response.setMessage("User deleted successfully");
			return response;
		} catch (Exception e) {
			response.setSuccess(false);
			return response;
		}
	}


	@ExceptionHandler(EntityNotFoundException.class)
	void handleUserNotFoundRequests(HttpServletResponse response) throws IOException {
		response.sendError(HttpStatus.NOT_FOUND.value(), "Email address not found. Signup to continue");
	}

	@ExceptionHandler(EmailVerificationException.class)
	void handleEmailVerfificationException(HttpServletResponse response) throws IOException {
		response.sendError(HttpStatus.BAD_REQUEST.value(), "Invalid email verify token for user");
	}

}
