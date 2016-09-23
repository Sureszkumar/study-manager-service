package com.study.manager.controller;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;

import com.study.manager.domain.Book;
import com.study.manager.service.UserCoursesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.study.manager.domain.Course;
import com.study.manager.domain.ServiceResponse;
import com.study.manager.domain.User;
import com.study.manager.service.CourseService;
import com.study.manager.service.UserService;
import com.study.manager.service.exception.EmailVerificationException;

@RestController
@RequestMapping(value = "/api")
public class ApiController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApiController.class);

	@Inject
	private UserService userService;

	@Inject
	private CourseService courseService;

	@Inject
	private UserCoursesService userCoursesService;

	/*----------------User services -------------------*/

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

	@RequestMapping(value = "/course/list", method = RequestMethod.GET)
	public List<Course> getAllCourses() {
		return courseService.getAll();
	}

	@RequestMapping(value = "/course/{courseId}", method = RequestMethod.GET)
	public Course getCourse( @PathVariable("courseId") final Long courseId) {
		return courseService.getCourse(courseId);
	}

	@RequestMapping(value = "/course/add", method = RequestMethod.POST)
	public ServiceResponse addCourse(@RequestBody final Course course) {
		ServiceResponse response = new ServiceResponse();
		try {
			courseService.addCourse(course);
			response.setSuccess(true);
			response.setMessage("Successfully added");
			return response;
		} catch (Exception e) {
			response.setSuccess(false);
			return response;
		}
	}

	@RequestMapping(value = "user/{userId}/subscribeCourses", method = RequestMethod.POST)
	public ServiceResponse subscribeCourses(@PathVariable("userId") final Long userId, @RequestBody final List<Long> courseIds) {
		ServiceResponse response = new ServiceResponse();
		try {
			userCoursesService.subscribeCourses(userId, courseIds);
			response.setSuccess(true);
			response.setMessage("Course successfully subscribed");
			return response;
		} catch (Exception e) {
			response.setSuccess(false);
			return response;
		}
	}

	@RequestMapping(value = "user/{userId}/course/list", method = RequestMethod.GET)
	public List<Course> getSubscribeCourses(@PathVariable("userId") final Long userId) {
		return userCoursesService.getSubscribeCourses(userId);
	}

	@RequestMapping(value = "user/{userId}/course/add", method = RequestMethod.POST)
	public ServiceResponse addCustomCourse(@PathVariable("userId") final Long userId, @RequestBody final Course course) {
		ServiceResponse response = new ServiceResponse();
		try {
			userCoursesService.addCustomCourse(userId, course);
			response.setSuccess(true);
			response.setMessage("Successfully added");
			return response;
		} catch (Exception e) {
			response.setSuccess(false);
			return response;
		}
	}
	@RequestMapping(value = "user/{userId}/course/{courseId}", method = RequestMethod.POST)
	public ServiceResponse addCustomBook(@PathVariable("userId") final Long userId, @PathVariable("courseId") final Long courseId,
										 @RequestBody final Book book) {
		ServiceResponse response = new ServiceResponse();
		try {
			userCoursesService.addCustomBook(userId, courseId, book);
			response.setSuccess(true);
			response.setMessage("Successfully added");
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
