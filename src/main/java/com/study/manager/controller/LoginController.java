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
import org.springframework.web.bind.annotation.RestController;

import com.study.manager.domain.User;
import com.study.manager.service.UserService;

@RestController
@RequestMapping(value = "/user")
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
    
    @Inject
    private UserService userService;
	    

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public User createUser(@RequestBody final User user) {
        LOGGER.debug("Received request to create the {}", user);
        return userService.create(user);
    }
    
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public User loginUser(@RequestBody final User user) {
        LOGGER.debug("Received request to create the {}", user);
        return userService.loginUser(user);
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
