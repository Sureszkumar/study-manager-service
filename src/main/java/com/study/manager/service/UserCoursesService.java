package com.study.manager.service;

import com.study.manager.domain.Book;
import com.study.manager.domain.Course;
import com.study.manager.entity.BookEntity;
import com.study.manager.entity.CourseBooksEntity;
import com.study.manager.entity.CourseEntity;
import com.study.manager.entity.UserCoursesEntity;
import com.study.manager.repository.BookRepository;
import com.study.manager.repository.CourseBooksRepository;
import com.study.manager.repository.CourseRepository;
import com.study.manager.repository.UserCoursesRepository;
import com.study.manager.translator.BookTranslator;
import com.study.manager.translator.CourseTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.inject.Inject;
import java.util.List;

@Service
@Validated
public class UserCoursesService {

	private Logger log = LoggerFactory.getLogger(UserCoursesService.class);
	
	@Inject
	private UserCoursesRepository userCoursesRepository;

	@Inject
	private CourseBooksRepository courseBooksRepository;

	@Inject
	private CourseRepository courseRepository;

	@Inject
	private CourseTranslator courseTranslator;

	@Inject
	private BookRepository bookRepository;
	@Inject
	private BookTranslator bookTranslator;

	public void subscribeCourses(Long userId, List<Long> courseIds) {
		for(Long courseId : courseIds) {
			UserCoursesEntity userCourseEntity = new UserCoursesEntity(userId, courseId);
			userCoursesRepository.save(userCourseEntity);
		}
	}

	public List<Course> getSubscribeCourses(Long userId) {
		List<Long> courseIds = userCoursesRepository.findAllCourses(userId);
		return courseTranslator.translateToDomain(courseRepository.findAll(courseIds));
	}

	public void addCustomCourse(Long userId, Course course) {
		List<BookEntity> bookEntityList = bookTranslator.translateToEntity(course.getBookList());
		for(BookEntity bookEntity : bookEntityList){
			bookRepository.save(bookEntity);
		}
		CourseEntity courseEntity = courseTranslator.translateToEntity(course);
		CourseEntity persistedEntity = courseRepository.save(courseEntity);
		userCoursesRepository.save(new UserCoursesEntity(userId, persistedEntity.getId()));

	}

	public void addCustomBook(Long userId, Long courseId, Book book) {
		BookEntity bookEntity = bookRepository.save(bookTranslator.translateToEntity(book));
		CourseBooksEntity courseBooksEntity = new CourseBooksEntity(courseId, bookEntity.getId());
		courseBooksRepository.save(courseBooksEntity);
		userCoursesRepository.save(new UserCoursesEntity(userId, courseBooksEntity.getId()));

	}
}
