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

import com.study.manager.domain.User;
import com.study.manager.service.UserService;

@RestController
@RequestMapping(value = "/api")
public class ApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiController.class);
    
    @Inject
    private UserService userService;
	    
    /*----------------User services -------------------*/

   /* @RequestMapping(value = "/user", method = RequestMethod.GET)
    public List<UserEntity> listUsers() {
        LOGGER.debug("Received request to list all users");
        return userService.getAll();
    }
*/
    @RequestMapping(value = "/user/id/{id}", method = RequestMethod.GET)
    public User getUser(@PathVariable("id") final Long id) {
        LOGGER.debug("Received request to retrieve user : {}", id);
        return userService.get(id);
    }
    
    @RequestMapping(value = "/user/email/{email}", method = RequestMethod.GET)
    public User findUserByEmail(@PathVariable("email") final String email) {
        LOGGER.debug("Received request to retrieve user : {}", email);
        return userService.findUserByEmail(email);
    }


   /* @RequestMapping(value = "/user", method = RequestMethod.PUT)
    public UserEntity updateUser(@RequestBody final UserEntity user) {
        LOGGER.debug("Received request to create the {}", user);
        return userService.update(user);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable("id") final Long userId) {
        LOGGER.debug("Received request to delete the {}", userId);
        userService.delete(userId);
    }*/
    
    @ExceptionHandler(EntityNotFoundException.class)
    void handleUserNotFoundRequests(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value(), "Email address not found. Signup to continue");
    }

}
