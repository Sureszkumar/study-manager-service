package com.study.manager.controller;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.study.manager.entity.UserEntity;
import com.study.manager.service.UserService;
import com.study.manager.service.exception.ServiceException;

@RestController
public class StudyTrackController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudyTrackController.class);
    
    @Inject
    private UserService userService;
    
    /*----------------User services -------------------*/

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public List<UserEntity> listUsers() {
        LOGGER.debug("Received request to list all users");
        return userService.getAll();
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public UserEntity getUser(@PathVariable("id") final Long id) {
        LOGGER.debug("Received request to retrieve user : {}", id);
        return userService.get(id);
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public UserEntity createUser(@RequestBody final UserEntity user) {
        LOGGER.debug("Received request to create the {}", user);
        return userService.create(user);
    }

    @RequestMapping(value = "/user", method = RequestMethod.PUT)
    public UserEntity updateUser(@RequestBody final UserEntity user) {
        LOGGER.debug("Received request to create the {}", user);
        return userService.update(user);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable("id") final Long userId) {
        LOGGER.debug("Received request to delete the {}", userId);
        userService.delete(userId);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleUserServiceException(ServiceException e) {
        return e.getMessage();
    }

}
