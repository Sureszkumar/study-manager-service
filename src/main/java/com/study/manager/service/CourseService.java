package com.study.manager.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.inject.Inject;

import com.study.manager.domain.Book;
import com.study.manager.repository.CourseBooksRepository;
import com.study.manager.repository.UserCoursesRepository;
import com.study.manager.translator.BookTranslator;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.study.manager.domain.Course;
import com.study.manager.entity.CourseEntity;
import com.study.manager.repository.CourseRepository;
import com.study.manager.translator.CourseTranslator;

@Service
@Validated
public class CourseService {

	@Inject
	private CourseRepository courseRepository;

	@Inject
	private CourseTranslator courseTranslator;

	@Inject
	private BookTranslator bookTranslator;

	@Inject
	private CourseBooksService courseBooksService;

	public List<Course> getAll() {
		List<CourseEntity> courseEntities = courseRepository.findAll();
		return courseTranslator.translateToDomain(courseEntities);
	}

	public void addCourse(Course course) {
		CourseEntity courseEntity = courseTranslator.translateToEntity(course);
		courseEntity.setCreationDateTime(LocalDateTime.now());
		courseEntity.setLastChangeTimestamp(LocalDateTime.now());
		courseRepository.save(courseEntity);
		
	}

	public Course getCourse(Long courseId) {
		CourseEntity courseEntity = courseRepository.findOne(courseId);
		List<Book> bookList = bookTranslator.translateToDomain(courseBooksService.findBooks(courseId));
		Course course = courseTranslator.translateToDomain(courseEntity);
		course.getBookList().addAll(bookList);
		return course;

	}
}
