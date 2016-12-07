package com.study.manager.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.study.manager.domain.User;
import com.study.manager.service.UserService;
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
import com.study.manager.domain.CourseSettings;
import com.study.manager.domain.Courses;
import com.study.manager.domain.Goal;
import com.study.manager.domain.ServiceResponse;
import com.study.manager.service.CourseService;
import com.study.manager.service.UserCoursesService;
import com.study.manager.service.exception.EmailVerificationException;
import com.study.manager.service.exception.ServiceException;

@RestController
@RequestMapping(value = "/api")
public class ApiController {

    @Inject
    private CourseService courseService;

    @Inject
    private UserCoursesService userCoursesService;

    @Inject
    private UserService userService;

    @RequestMapping(value = "/courses", method = RequestMethod.GET)
    public List<Course> getAllCourses(@RequestHeader("user-id") long userId) {
        return courseService.getAll(userId);
    }

    @RequestMapping(value = "/course/{courseId}", method = RequestMethod.GET)
    public Course getCourse(@PathVariable("courseId") final Long courseId, @RequestHeader("user-id") long userId) {
        return courseService.getCourse(userId, courseId);
    }

    @RequestMapping(value = "/subscribeCourse/{courseId}", method = RequestMethod.POST)
    public ServiceResponse subscribeCourse(@PathVariable("courseId") final Long courseId,
                                           @RequestHeader("user-id") long userId, @RequestBody Course course) {
        ServiceResponse response = new ServiceResponse();
        try {
            userCoursesService.subscribeCourse(userId, courseId, course.getStartDate());
            response.setSuccess(true);
            response.setMessage("Course successfully subscribed");
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

    @RequestMapping(value = "/subscribedCourses", method = RequestMethod.GET)
    public Courses getSubscribedCourses(@RequestHeader("user-id") final Long userId) {
        Courses courses = new Courses();
        courses.setUserName(userService.get(userId).getName());
        List<Course> subscribedCourses = userCoursesService.getSubscribedCourses(userId);
        courses.setCourses(subscribedCourses);
        if (subscribedCourses != null && !subscribedCourses.isEmpty()) {
            LocalDate lastUpdateDate = subscribedCourses.stream().map(c -> c.getLastUpdatedDate())
                    .max(LocalDate::compareTo).get();
            courses.setLastUpdatedDate(lastUpdateDate);
        }

        return courses;
    }

    @RequestMapping(value = "/subscribedCourses/updatePriority/{from}/{to}", method = RequestMethod.POST)
    public ServiceResponse updatePriorityForSubscribedCourses(@RequestHeader("user-id") final Long userId,
                                                              @PathVariable("from") final int from,
                                                              @PathVariable("to") final int to) {
        ServiceResponse response = new ServiceResponse();
        try {
            userCoursesService.updatePriorityForSubscribedCourses(userId, from, to);
            response.setSuccess(true);
            response.setMessage("Course Priority successfully updated");
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

    @RequestMapping(value = "/subscribedCourse/{courseId}", method = RequestMethod.GET)
    public Course getSubscribedCourse(@PathVariable("courseId") final Long courseId,
                                      @RequestHeader("user-id") final Long userId) {
        return userCoursesService.getSubscribedCourse(userId, courseId);
    }

    @RequestMapping(value = "/updateSubscribedCourse/{courseId}", method = RequestMethod.POST)
    public ServiceResponse updateSubscribedCourse(@RequestHeader("user-id") long userId,
                                                  @PathVariable("courseId") final Long courseId, @RequestBody Goal goal) {
        ServiceResponse response = new ServiceResponse();
        try {
            userCoursesService.updateSubscribedCourseGoal(userId, courseId, goal);
            response.setSuccess(true);
            response.setMessage("Subscribed Course goal successfully updated");
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

    @RequestMapping(value = "/subscribedCourse/{courseId}/settings", method = RequestMethod.GET)
    public CourseSettings getSubscribedCourseSettings(@RequestHeader("user-id") long userId,
                                                      @PathVariable("courseId") final Long courseId) {
        return userCoursesService.getSubscribedCourseSettings(userId, courseId);
    }

    @RequestMapping(value = "/subscribedCourse/{courseId}/settings", method = RequestMethod.POST)
    public ServiceResponse updateSubscribedCourseSettings(@RequestHeader("user-id") long userId,
                                                          @PathVariable("courseId") final Long courseId, @RequestBody CourseSettings courseSettings) {
        ServiceResponse response = new ServiceResponse();
        try {
            userCoursesService.updateSubscribedCourseSettings(userId, courseId, courseSettings);
            response.setSuccess(true);
            response.setMessage("Course settings successfully updated");
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


    @RequestMapping(value = "/subscribedCourse/{courseId}/updateStartDate", method = RequestMethod.POST)
    public ServiceResponse updateSubscribedStartDate(@RequestHeader("user-id") long userId,
                                                          @PathVariable("courseId") final Long courseId,
                                                          @RequestBody Course course) {
        ServiceResponse response = new ServiceResponse();
        try {
            userCoursesService.updateSubscribedStartDate(userId, courseId, course.getStartDate());
            response.setSuccess(true);
            response.setMessage("Course start date successfully set");
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

    @RequestMapping(value = "/unSubscribeCourse/{courseId}", method = RequestMethod.POST)
    public ServiceResponse unSubscribeCourse(@PathVariable("courseId") final Long courseId,
                                             @RequestHeader("user-id") long userId) {
        ServiceResponse response = new ServiceResponse();
        try {
            userCoursesService.unSubscribeCourse(userId, courseId);
            response.setSuccess(true);
            response.setMessage("Course successfully unSubscribed");
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

    @RequestMapping(value = "/addCustomCourse", method = RequestMethod.POST)
    public ServiceResponse addCustomCourse(@RequestHeader("user-id") final Long userId,
                                           @RequestBody final Course course) {
        ServiceResponse response = new ServiceResponse();
        try {
            userCoursesService.addCustomCourse(userId, course);
            response.setSuccess(true);
            response.setMessage("Successfully added");
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

    @RequestMapping(value = "/subscribedCourse/{courseId}/addCustomBook", method = RequestMethod.POST)
    public ServiceResponse addCustomBook(@RequestHeader("user-id") final Long userId,
                                         @PathVariable("courseId") final Long courseId, @RequestBody final Book book) {
        ServiceResponse response = new ServiceResponse();
        try {
            userCoursesService.addCustomBook(userId, courseId, book);
            response.setSuccess(true);
            response.setMessage("Successfully added");
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

    @RequestMapping(value = "/subscribedCourse/{courseId}/book/{bookId}", method = RequestMethod.DELETE)
    public ServiceResponse deleteBook(@RequestHeader("user-id") final Long userId,
                                      @PathVariable("courseId") final Long courseId, @PathVariable("bookId") final Long bookId) {
        ServiceResponse response = new ServiceResponse();
        try {
            userCoursesService.deleteBook(userId, courseId, bookId);
            response.setSuccess(true);
            response.setMessage("Successfully book deleted");
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

    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public ServiceResponse resetPassword(@RequestHeader("user-id") final Long userId, @RequestBody final User user) {
        ServiceResponse response = new ServiceResponse();
        try {
            userService.resetPassword(userId, user.getPassword());
            response.setSuccess(true);
            response.setMessage("Successfully password reset");
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

    @RequestMapping(value = "/getUserProfile", method = RequestMethod.GET)
    public User getUserProfile(@RequestHeader("user-id") final Long userId) {
        User user = userService.getUserProfile(userId);
        return user;
    }

    @RequestMapping(value = "/updateUserProfile", method = RequestMethod.POST)
    public ServiceResponse updateUserProfile(@RequestHeader("user-id") final Long userId,
                                             @RequestBody final User user) {
        ServiceResponse response = new ServiceResponse();
        try {
            userService.updateUserProfile(userId, user);
            response.setSuccess(true);
            response.setMessage("Successfully added");
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

    @ExceptionHandler(EntityNotFoundException.class)
    void handleUserNotFoundRequests(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value(), "Email address not found. Signup to continue");
    }

    @ExceptionHandler(EmailVerificationException.class)
    void handleEmailVerfificationException(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), "Invalid email verify token for user");
    }

}
