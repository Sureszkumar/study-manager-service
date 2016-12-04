package com.study.manager.translator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import com.study.manager.domain.Book;
import com.study.manager.entity.UserCourseBooksEntity;
import org.springframework.stereotype.Component;

import com.study.manager.domain.Course;
import com.study.manager.domain.Type;
import com.study.manager.entity.CourseEntity;
import com.study.manager.entity.UserCoursesEntity;
import com.study.manager.repository.UserCoursesRepository;

@Component
public class CourseTranslator {

	@Inject
	private UserCoursesRepository userCoursesRepository;

	public List<Course> translateToDomain(List<CourseEntity> courseEntities) {

		List<Course> courseList = new ArrayList<>();
		for (CourseEntity courseEntity : courseEntities) {
			Course course = new Course();
			long courseId = courseEntity.getId();
			course.setId(courseId);
			course.setTitle(courseEntity.getTitle());
			course.setDescription(courseEntity.getDescription());
			course.setType(courseEntity.getType().name());
			course.setSubscribed(true);
			course.setPreparationTimeInWeeks(courseEntity.getDefaultTimeInWeeks());
			course.setPreparationTimeInMonths(courseEntity.getDefaultTimeInMonths());
			courseList.add(course);
		}
		return courseList;
	}

	public List<Course> translateToDomain(List<CourseEntity> courseEntities, Long userId) {

		List<Course> courseList = new ArrayList<>();
		for (CourseEntity courseEntity : courseEntities) {
			Course course = new Course();
			long courseId = courseEntity.getId();
			UserCoursesEntity userCoursesEntity = userCoursesRepository.findBy(userId, courseId);
			List<UserCourseBooksEntity> userCourseBooksEntities = userCoursesEntity.getUserCourseBooksEntity();
			UserCourseBooksEntity userCourseBooksEntity = userCourseBooksEntities.stream().max(Comparator.comparing(UserCourseBooksEntity::getLastChangeTimestamp)).get();
			List<Book> bookList = new ArrayList<>();
			Book book = new Book();
			book.setNoOfPages(userCourseBooksEntity.getTotalNoOfPages());
			book.setType(userCourseBooksEntity.getType().name());
			book.setAuthor(userCourseBooksEntity.getAuthor());
			book.setTitle(userCourseBooksEntity.getTitle());
			book.setId(userCourseBooksEntity.getId());
			book.setNoOfPagesRead(userCourseBooksEntity.getNoOfPagesRead());
			book.setNoOfPagesUnRead(userCourseBooksEntity.getNoOfPagesUnRead());
			book.setImageUrl(userCourseBooksEntity.getImageUrl());
			book.setRevisionCompleted(userCourseBooksEntity.isRevisionCompleted());
			bookList.add(book);
			course.setBookList(bookList);
			course.setId(courseId);
			course.setTitle(courseEntity.getTitle());
			course.setDescription(courseEntity.getDescription());
			course.setType(courseEntity.getType().name());
			course.setStartDate(userCoursesEntity.getStartDate());
			course.setEndDate(userCoursesEntity.getEndDate());
			course.setCurrentStatus(userCoursesEntity.getCurrentStatus());
			course.setCompletionRate(BigDecimal.valueOf(userCoursesEntity.getCompletionRate()));
			course.setTodayGoal(userCoursesEntity.getTodayGoal());
			course.setLastUpdatedDate(userCoursesEntity.getLastChangeTimestamp() != null ?
					userCoursesEntity.getLastChangeTimestamp().toLocalDate() : null);
			courseList.add(course);
		}
		return courseList;
	}

	public Course translateToDomain(CourseEntity courseEntity) {

		Course course = new Course();
		course.setId(courseEntity.getId());
		course.setTitle(courseEntity.getTitle());
		course.setDescription(courseEntity.getDescription());
		course.setType(courseEntity.getType().name());
		return course;
	}

	public CourseEntity translateToEntity(Course course) {
		CourseEntity courseEntity = new CourseEntity();
		courseEntity.setTitle(course.getTitle());
		courseEntity.setDescription(course.getDescription());
		courseEntity.setType(Type.valueOf(course.getType()));
		return courseEntity;

	}

}
