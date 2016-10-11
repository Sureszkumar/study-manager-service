package com.study.manager.service;

import java.time.LocalDate;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.study.manager.domain.Book;
import com.study.manager.domain.Course;
import com.study.manager.domain.CourseSettings;
import com.study.manager.domain.CourseStatus;
import com.study.manager.domain.Proficiency;
import com.study.manager.domain.WeeklyHours;
import com.study.manager.entity.BookEntity;
import com.study.manager.entity.CourseBooksEntity;
import com.study.manager.entity.CourseEntity;
import com.study.manager.entity.UserCoursesEntity;
import com.study.manager.entity.WeekEntity;
import com.study.manager.entity.WeeklyHoursEntity;
import com.study.manager.entity.WeeklyPagesEntity;
import com.study.manager.repository.BookRepository;
import com.study.manager.repository.CourseBooksRepository;
import com.study.manager.repository.CourseRepository;
import com.study.manager.repository.UserCoursesRepository;
import com.study.manager.translator.BookTranslator;
import com.study.manager.translator.CourseSettingsTranslator;
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
	@Inject
	private CourseSettingsTranslator courseSettingsTranslator;

	public void subscribeCourse(Long userId, Long courseId) {
		UserCoursesEntity userCourseEntity = new UserCoursesEntity();
		userCourseEntity.setUserId(userId);
		userCourseEntity.setCourseId(courseId);
		userCourseEntity.setStartDate(LocalDate.now());
		userCourseEntity.setCompletionRate(0);
		userCourseEntity.setCurrentStatus(CourseStatus.ON_TRACK.name());
		int defaultTimeInWeeks = courseRepository.findOne(courseId).getDefaultTimeInWeeks();
		userCourseEntity.setEndDate(LocalDate.now().plusWeeks(defaultTimeInWeeks));
		userCourseEntity.setProficiency(Proficiency.EASY.name());
		WeeklyHoursEntity weeklyHoursEntity = new WeeklyHoursEntity();
		WeekEntity weekEntity = new WeekEntity(1, 1, 1, 1, 1, 1, 1);
		weeklyHoursEntity.setWeekEntity(weekEntity);
		userCourseEntity.setWeeklyHoursEntity(weeklyHoursEntity);
		WeeklyPagesEntity weeklyPagesEntity = new WeeklyPagesEntity();
		WeekEntity weekEntityPages = new WeekEntity(18, 18, 18, 18, 18, 18, 18);
		weeklyPagesEntity.setWeekEntity(weekEntityPages);
		userCourseEntity.setWeeklyPagesEntity(weeklyPagesEntity);
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

	public CourseSettings getCourseSettings(long userId, Long courseId) {
		UserCoursesEntity userCoursesEntity = userCoursesRepository.findBy(userId, courseId);
		return courseSettingsTranslator.translateToDomain(userCoursesEntity);
	}

	public void updateCourseSettings(long userId, Long courseId, CourseSettings courseSettings) {
		UserCoursesEntity userCoursesEntity = userCoursesRepository.findBy(userId, courseId);
		WeeklyHours weeklyHours = courseSettings.getWeeklyHours();
		WeekEntity weekEntity = new WeekEntity(weeklyHours.getMonday(), weeklyHours.getTuesday(), 
				weeklyHours.getWednesday(), weeklyHours.getThursday(), weeklyHours.getFriday(), 
				weeklyHours.getSaturday(), weeklyHours.getSunday());
		userCoursesEntity.getWeeklyHoursEntity().setWeekEntity(weekEntity);
		userCoursesRepository.save(userCoursesEntity);
	}
}
