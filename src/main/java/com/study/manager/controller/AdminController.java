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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.study.manager.domain.Book;
import com.study.manager.domain.Course;
import com.study.manager.domain.ServiceResponse;
import com.study.manager.domain.User;
import com.study.manager.entity.ProficiencyEntity;
import com.study.manager.entity.EntityType;
import com.study.manager.service.BookService;
import com.study.manager.service.CourseBooksService;
import com.study.manager.service.CourseProficiencyService;
import com.study.manager.service.CourseService;
import com.study.manager.service.ImageService;
import com.study.manager.service.UserService;
import com.study.manager.service.exception.EmailVerificationException;

@RestController
@RequestMapping(value = "/admin")
public class AdminController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

	@Inject
	private UserService userService;

	@Inject
	private CourseService courseService;

	@Inject
	private BookService bookService;

	@Inject
	private CourseBooksService courseBooksService;

	@Inject
	private ImageService imageService;

	@Inject
	private CourseProficiencyService courseProficiencyService;

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

	@RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
	public User get(@PathVariable("id") final Long id) {
		LOGGER.debug("Received request to retrieve user : {}", id);
		try {
			User user = userService.get(id);
			// response.setSuccess(true);
			// response.setMessage("User retreived successfully");
			return user;
		} catch (Exception e) {
			// response.setSuccess(false);
			return null;
		}
	}

	@RequestMapping(value = "/verifyUser/{userId}", method = RequestMethod.POST)
	public ServiceResponse verifyUser(@PathVariable("userId") final Long userId) {
		ServiceResponse response = new ServiceResponse();
		try {
			userService.verifyUser(userId);
			response.setSuccess(true);
			response.setMessage("Successfully added");
			return response;
		} catch (Exception e) {
			response.setSuccess(false);
			return response;
		}
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

	@RequestMapping(value = "/courseProficiency/add", method = RequestMethod.POST)
	public ServiceResponse addCourse(@RequestBody final ProficiencyEntity courseProficiencyEntity) {
		ServiceResponse response = new ServiceResponse();
		try {
			courseProficiencyService.add(courseProficiencyEntity);
			response.setSuccess(true);
			response.setMessage("Successfully added");
			return response;
		} catch (Exception e) {
			response.setSuccess(false);
			return response;
		}
	}

	@RequestMapping(value = "/book/add", method = RequestMethod.POST)
	public ServiceResponse addBook(@RequestBody final Book book) {
		ServiceResponse response = new ServiceResponse();
		try {
			bookService.addBook(book);
			response.setSuccess(true);
			response.setMessage("Successfully added");
			return response;
		} catch (Exception e) {
			response.setSuccess(false);
			return response;
		}
	}

	@RequestMapping(value = "addImage/book/{bookId}", consumes = { "multipart/form-data" }, method = RequestMethod.POST)
	public ServiceResponse addBookImage(@RequestParam final Long bookId, @RequestPart("image") MultipartFile image) {
		ServiceResponse response = new ServiceResponse();
		try {
			imageService.add(image.getBytes(), bookId, EntityType.BOOK);
			response.setSuccess(true);
			response.setMessage("Successfully added");
			return response;
		} catch (Exception e) {
			response.setSuccess(false);
			return response;
		}
	}

	@RequestMapping(value = "addImage/user/{userId}", consumes = { "multipart/form-data" }, method = RequestMethod.POST)
	public ServiceResponse addUserImage(@RequestParam final Long userId, @RequestPart("image") MultipartFile image) {
		ServiceResponse response = new ServiceResponse();
		try {
			imageService.add(image.getBytes(), userId, EntityType.USER);
			response.setSuccess(true);
			response.setMessage("Successfully added");
			return response;
		} catch (Exception e) {
			response.setSuccess(false);
			return response;
		}
	}

	@RequestMapping(value = "addImage/course/{courseId}", consumes = {
			"multipart/form-data" }, method = RequestMethod.POST)
	public ServiceResponse addCourseImage(@RequestParam final Long courseId,
			@RequestPart("image") MultipartFile image) {
		ServiceResponse response = new ServiceResponse();
		try {
			imageService.add(image.getBytes(), courseId, EntityType.COURSE);
			response.setSuccess(true);
			response.setMessage("Successfully added");
			return response;
		} catch (Exception e) {
			response.setSuccess(false);
			return response;
		}
	}

	@RequestMapping(value = "/book/{id}", method = RequestMethod.GET)
	public Book getBook(@PathVariable("id") final Long id) {
		LOGGER.debug("Received request to retrieve user : {}", id);
		try {
			Book book = bookService.get(id);
			// response.setSuccess(true);
			// response.setMessage("User retreived successfully");
			return book;
		} catch (Exception e) {
			// response.setSuccess(false);
			return null;
		}
	}

	@RequestMapping(value = "/link/course/{courseId}/book/{bookId}", method = RequestMethod.POST)
	public ServiceResponse linkCourseBook(@PathVariable("courseId") final Long courseId,
			@PathVariable("courseId") final Long bookId) {
		ServiceResponse response = new ServiceResponse();
		try {
			courseBooksService.linkCourseBook(courseId, bookId);
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
