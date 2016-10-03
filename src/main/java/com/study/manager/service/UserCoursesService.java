package com.study.manager.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

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

@Service
@Validated
public class UserCoursesService {

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

	public void subscribeCourse(Long userId, Long courseId) {
		UserCoursesEntity userCourseEntity = new UserCoursesEntity(userId, courseId);
		userCoursesRepository.save(userCourseEntity);
	}

	public void unSubscribeCourse(Long userId, Long courseId) {
		UserCoursesEntity userCourseEntity = userCoursesRepository.findBy(userId, courseId);
		userCoursesRepository.delete(userCourseEntity);
	}

	public List<Course> getSubscribedCourses(Long userId) {
		List<Long> courseIds = userCoursesRepository.findAllCourses(userId);
		return courseTranslator.translateToDomain(courseRepository.findAll(courseIds));
	}

	public void addCustomCourse(Long userId, Course course) {
		if (course.getBookList() != null) {
			List<BookEntity> bookEntityList = bookTranslator.translateToEntity(course.getBookList());
			for (BookEntity bookEntity : bookEntityList) {
				bookRepository.save(bookEntity);
			}
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

	public boolean isSubscribed(long courseId, long userId) {
		Long count = userCoursesRepository.findCount(userId, courseId);
		return count == 0 ? false : true;
	}
}
