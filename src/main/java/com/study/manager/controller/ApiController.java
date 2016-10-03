package com.study.manager.controller;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.study.manager.domain.Book;
import com.study.manager.domain.Course;
import com.study.manager.domain.ServiceResponse;
import com.study.manager.service.CourseService;
import com.study.manager.service.UserCoursesService;
import com.study.manager.service.exception.EmailVerificationException;

@RestController
@RequestMapping(value = "/api")
public class ApiController {

	//private static final Logger LOGGER = LoggerFactory.getLogger(ApiController.class);

	/*@Inject
	private UserService userService;*/

	@Inject
	private CourseService courseService;

	@Inject
	private UserCoursesService userCoursesService;

	/*----------------User services -------------------*/

	/*@RequestMapping(value = "/user/id/{id}", method = RequestMethod.GET)
	public User getUser(@PathVariable("id") final Long id) {
		LOGGER.debug("Received request to retrieve user : {}", id);
		return userService.get(id);
	}

	@RequestMapping(value = "/user/email/{email}", method = RequestMethod.GET)
	public User findUserByEmail(@PathVariable("email") final String email) {
		LOGGER.debug("Received request to retrieve user : {}", email);
		return userService.findUserByEmail(email);
	}
*/
	@RequestMapping(value = "/courses", method = RequestMethod.GET)
	public List<Course> getAllCourses(@RequestHeader("user-id") long userId) {
		return courseService.getAll(userId);
	}

	@RequestMapping(value = "/course/{courseId}", method = RequestMethod.GET)
	public Course getCourse( @PathVariable("courseId") final Long courseId) {
		return courseService.getCourse(courseId);
	}

	
	@RequestMapping(value = "/subscribeCourse/{courseId}", method = RequestMethod.POST)
	public ServiceResponse subscribeCourse(@PathVariable("courseId") final Long courseId, 
			@RequestHeader("user-id") long userId) {
		ServiceResponse response = new ServiceResponse();
		try {
			userCoursesService.subscribeCourse(userId, courseId);
			response.setSuccess(true);
			response.setMessage("Course successfully subscribed");
			return response;
		} catch (Exception e) {
			response.setSuccess(false);
			response.setMessage(e.getMessage());
			return response;
		}
	}

	@RequestMapping(value = "/unSubscribeCourse/{courseId}", method = RequestMethod.POST)
	public ServiceResponse unSubscribeCourse(@PathVariable("courseId") final Long courseId, 
			@RequestHeader("user-id") long userId) {
		ServiceResponse response = new ServiceResponse();
		try {
			userCoursesService.unSubscribeCourse(userId, courseId);
			response.setSuccess(true);
			response.setMessage("Course successfully unSubscribed");
			return response;
		} catch (Exception e) {
			response.setSuccess(false);
			response.setMessage(e.getMessage());
			return response;
		}
	}
	
	@RequestMapping(value = "/subscribedCourses", method = RequestMethod.GET)
	public List<Course> getSubscribedCourses(@RequestHeader("user-id") final Long userId) {
		return userCoursesService.getSubscribedCourses(userId);
	}

	@RequestMapping(value = "/addCustomCourse", method = RequestMethod.POST)
	public ServiceResponse addCustomCourse(@RequestHeader("user-id") final Long userId, @RequestBody final Course course) {
		ServiceResponse response = new ServiceResponse();
		try {
			userCoursesService.addCustomCourse(userId, course);
			response.setSuccess(true);
			response.setMessage("Successfully added");
			return response;
		} catch (Exception e) {
			response.setSuccess(false);
			response.setMessage(e.getMessage());
			return response;
		}
	}
	@RequestMapping(value = "/course/{courseId}/addCustomBook", method = RequestMethod.POST)
	public ServiceResponse addCustomBook(@RequestHeader("user-id") final Long userId, @PathVariable("courseId") final Long courseId,
										 @RequestBody final Book book) {
		ServiceResponse response = new ServiceResponse();
		try {
			userCoursesService.addCustomBook(userId, courseId, book);
			response.setSuccess(true);
			response.setMessage("Successfully added");
			return response;
		} catch (Exception e) {
			response.setSuccess(false);
			response.setMessage(e.getMessage());
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
