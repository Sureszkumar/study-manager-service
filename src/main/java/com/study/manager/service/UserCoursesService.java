package com.study.manager.service;

import static java.time.temporal.ChronoUnit.DAYS;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.study.manager.domain.Book;
import com.study.manager.domain.Course;
import com.study.manager.domain.CourseSettings;
import com.study.manager.domain.CourseStatus;
import com.study.manager.domain.Goal;
import com.study.manager.domain.Proficiency;
import com.study.manager.domain.WeeklyHours;
import com.study.manager.entity.BookEntity;
import com.study.manager.entity.CourseBooksEntity;
import com.study.manager.entity.CourseEntity;
import com.study.manager.entity.CourseProficiencyEntity;
import com.study.manager.entity.UserCourseBooksEntity;
import com.study.manager.entity.UserCoursesEntity;
import com.study.manager.entity.WeekEntity;
import com.study.manager.entity.WeeklyHoursEntity;
import com.study.manager.entity.WeeklyPagesEntity;
import com.study.manager.repository.BookRepository;
import com.study.manager.repository.CourseBooksRepository;
import com.study.manager.repository.CourseProficiencyRepository;
import com.study.manager.repository.CourseRepository;
import com.study.manager.repository.UserCoursesRepository;
import com.study.manager.service.exception.ServiceException;
import com.study.manager.translator.BookTranslator;
import com.study.manager.translator.CourseSettingsTranslator;
import com.study.manager.translator.CourseTranslator;

@Service
@Validated
public class UserCoursesService {

	public static final int DEFAULT_EASY_PAGES = 15;
	@Inject
	private UserCoursesRepository userCoursesRepository;

	@Inject
	private CourseBooksRepository courseBooksRepository;

	@Inject
	private CourseProficiencyRepository courseProficiencyRepository;

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
		List<Long> bookIds = courseBooksRepository.findBookIds(courseId);
		List<BookEntity> books = bookRepository.findAll(bookIds);
		List<UserCourseBooksEntity> userCourseBooksEntities = new ArrayList<>();
		int totalNoOfPages = 0;
		for (BookEntity book : books) {
			UserCourseBooksEntity userCourseBooksEntity = new UserCourseBooksEntity();
			userCourseBooksEntity.setAuthor(book.getAuthor());
			userCourseBooksEntity.setDescription(book.getDescription());
			userCourseBooksEntity.setIsbn(book.getIsbn());
			userCourseBooksEntity.setTotalNoOfPages(book.getNoOfPages());
			userCourseBooksEntity.setNoOfPagesRead(0);
			userCourseBooksEntity.setNoOfPagesUnRead(book.getNoOfPages());
			userCourseBooksEntity.setTitle(book.getTitle());
			userCourseBooksEntity.setType(book.getType());
			totalNoOfPages = +book.getNoOfPages();
			userCourseBooksEntities.add(userCourseBooksEntity);
		}
		userCourseEntity.setUserCourseBooksEntity(userCourseBooksEntities);
		userCourseEntity.setTotalNoOfPages(totalNoOfPages);
		userCourseEntity.setPagesUnRead(totalNoOfPages);
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
		CourseProficiencyEntity courseProficiencyEntity = courseProficiencyRepository.findByCourseId(courseId);
		int easyPages = DEFAULT_EASY_PAGES;
		if (courseProficiencyEntity != null) {
			easyPages = courseProficiencyEntity.getEasyPages();
		}
		WeekEntity weekEntityPages = new WeekEntity(easyPages);
		weeklyPagesEntity.setWeekEntity(weekEntityPages);
		userCourseEntity.setWeeklyPagesEntity(weeklyPagesEntity);
		userCourseEntity.setTodayGoal(weekEntityPages.getTodayGoal(LocalDate.now()));
		userCoursesRepository.save(userCourseEntity);
	}

	public void unSubscribeCourse(Long userId, Long courseId) {
		UserCoursesEntity userCourseEntity = userCoursesRepository.findBy(userId, courseId);
		userCoursesRepository.delete(userCourseEntity);
	}

	public List<Course> getSubscribedCourses(Long userId) {
		List<Long> courseIds = userCoursesRepository.findAllCourses(userId);
		return courseTranslator.translateToDomain(courseRepository.findAll(courseIds), userId);
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
		if (userCoursesEntity == null) {
			throw new ServiceException("User : " + userId + " is not subscribed to course : " + courseId);
		}
		return courseSettingsTranslator.translateToDomain(userCoursesEntity);
	}

	public void updateSubscribedCourseGoal(long userId, Long courseId, Goal goal) {
		UserCoursesEntity userCoursesEntity = userCoursesRepository.findBy(userId, courseId);
		List<UserCourseBooksEntity> userCourseBooksEntities = userCoursesEntity.getUserCourseBooksEntity();
		for (UserCourseBooksEntity userCourseBooksEntity : userCourseBooksEntities) {
			if (userCourseBooksEntity.getId().equals(goal.getBookId())) {
				userCourseBooksEntity
						.setNoOfPagesRead(userCourseBooksEntity.getNoOfPagesRead() + goal.getNoOfPagesRead());
				userCourseBooksEntity.setNoOfPagesUnRead(
						userCourseBooksEntity.getTotalNoOfPages() - userCourseBooksEntity.getNoOfPagesRead());
				userCoursesEntity.setTodayGoal(userCoursesEntity.getTodayGoal() - goal.getNoOfPagesRead());
				userCoursesEntity.setCompletionRate(
						userCourseBooksEntity.getNoOfPagesRead() / userCourseBooksEntity.getTotalNoOfPages() * 100);
			}
		}
		userCoursesRepository.save(userCoursesEntity);

	}

	public void updateCourseSettings(long userId, Long courseId, CourseSettings courseSettings) {
		UserCoursesEntity userCoursesEntity = userCoursesRepository.findBy(userId, courseId);
		WeeklyHours newWeeklyHours = courseSettings.getWeeklyHours();
		LocalDate newTargetDate = courseSettings.getTargetDate();
		if (newTargetDate != null) {
			int pagesUnRead = userCoursesEntity.getPagesUnRead();
			long noOfDaysLeft = DAYS.between(LocalDate.now(), newTargetDate);
			int dailyPagesToRead = (int) (pagesUnRead / noOfDaysLeft);
			WeekEntity weekDayPages = new WeekEntity(dailyPagesToRead);
			userCoursesEntity.getWeeklyPagesEntity().setWeekEntity(weekDayPages);
			WeekEntity weekDayHours = new WeekEntity(0);
			userCoursesEntity.getWeeklyHoursEntity().setWeekEntity(weekDayHours);
			userCoursesEntity.setTodayGoal(dailyPagesToRead);
			userCoursesEntity.setEndDate(newTargetDate);

		} else {

			if (newWeeklyHours != null) {
				WeekEntity weekDayHours = new WeekEntity(newWeeklyHours.getMonday(), newWeeklyHours.getTuesday(),
						newWeeklyHours.getWednesday(), newWeeklyHours.getThursday(), newWeeklyHours.getFriday(),
						newWeeklyHours.getSaturday(), newWeeklyHours.getSunday());
				userCoursesEntity.getWeeklyHoursEntity().setWeekEntity(weekDayHours);

				WeekEntity weekDayPages = getWeekDayPages(courseId, userCoursesEntity.getProficiency(), weekDayHours);
				userCoursesEntity.getWeeklyPagesEntity().setWeekEntity(weekDayPages);
				int totalPagesPerWeek = weekDayPages.getTotalWeekCount();
				int weeksToComplete = userCoursesEntity.getPagesUnRead() / totalPagesPerWeek;
				long daysToComplete = (int) Math.ceil(weeksToComplete) * 7;
				LocalDate newEndDate = userCoursesEntity.getStartDate().plusDays(daysToComplete);
				userCoursesEntity.setEndDate(getAbsoluteEndDate(newEndDate, weekDayHours));

			}
			if (courseSettings.getProficiency() != null
					&& !courseSettings.getProficiency().equals(userCoursesEntity.getProficiency())) {
				userCoursesEntity.setProficiency(courseSettings.getProficiency());
				String newProficiency = userCoursesEntity.getProficiency();
				WeekEntity weekDayPages = getWeekDayPages(courseId, newProficiency,
						userCoursesEntity.getWeeklyHoursEntity().getWeekEntity());
				userCoursesEntity.getWeeklyPagesEntity().setWeekEntity(weekDayPages);
				int totalPagesPerWeek = weekDayPages.getTotalWeekCount();
				double weeksToComplete = userCoursesEntity.getPagesUnRead() / totalPagesPerWeek;
				long daysToComplete = (int) Math.ceil(weeksToComplete) * 7;
				LocalDate newEndDate = userCoursesEntity.getStartDate().plusDays(daysToComplete);
				userCoursesEntity.setEndDate(
						getAbsoluteEndDate(newEndDate, userCoursesEntity.getWeeklyHoursEntity().getWeekEntity()));
			}
		}
		userCoursesRepository.save(userCoursesEntity);
	}

	public static LocalDate getAbsoluteEndDate(LocalDate endDate, WeekEntity weekDayHours) {
		DayOfWeek actualDayOfWeek = weekDayHours.getNonZeroDay();
		return endDate.with(TemporalAdjusters.next(actualDayOfWeek));
	}

	private WeekEntity getWeekDayPages(Long courseId, String proficiency, WeekEntity weekDayHours) {
		CourseProficiencyEntity courseProficiencyEntity = courseProficiencyRepository.findByCourseId(courseId);
		int pagesPerHour = 0;
		if (proficiency.equals(Proficiency.EASY.name())) {
			pagesPerHour = courseProficiencyEntity.getEasyPages();
		} else if (proficiency.equals(Proficiency.NORMAL.name())) {
			pagesPerHour = courseProficiencyEntity.getNormalPages();
		} else if (proficiency.equals(Proficiency.NORMAL.name())) {
			pagesPerHour = courseProficiencyEntity.getNormalPages();
		}
		WeekEntity weekDayPages = new WeekEntity();
		weekDayPages.setMonday(weekDayHours.getMonday() * pagesPerHour);
		weekDayPages.setTuesday(weekDayHours.getTuesday() * pagesPerHour);
		weekDayPages.setWednesday(weekDayHours.getWednesday() * pagesPerHour);
		weekDayPages.setThursday(weekDayHours.getThursday() * pagesPerHour);
		weekDayPages.setFriday(weekDayHours.getFriday() * pagesPerHour);
		weekDayPages.setSaturday(weekDayHours.getSaturday() * pagesPerHour);
		weekDayPages.setSunday(weekDayHours.getSunday() * pagesPerHour);
		return weekDayPages;
	}

	public Course getSubscribedCourse(Long userId, Long courseId) {
		UserCoursesEntity userCoursesEntity = userCoursesRepository.findBy(userId, courseId);
		CourseEntity courseEntity = courseRepository.findOne(courseId);
		Course course = courseTranslator.translateToDomain(courseEntity);
		List<UserCourseBooksEntity> userCourseBooksEntities = userCoursesEntity.getUserCourseBooksEntity();
		List<Book> bookList = new ArrayList<>();
		for (UserCourseBooksEntity userCourseBooksEntity : userCourseBooksEntities) {
			Book book = new Book();
			book.setNoOfPages(userCourseBooksEntity.getTotalNoOfPages());
			book.setType(userCourseBooksEntity.getType().name());
			book.setAuthor(userCourseBooksEntity.getAuthor());
			book.setTitle(userCourseBooksEntity.getTitle());
			book.setId(userCourseBooksEntity.getId());
			book.setNoOfPagesRead(userCourseBooksEntity.getNoOfPagesRead());
			book.setNoOfPagesUnRead(userCourseBooksEntity.getNoOfPagesUnRead());
			bookList.add(book);
		}
		course.setTodayGoal(userCoursesEntity.getTodayGoal());
		course.setBookList(bookList);
		return course;

	}
}
