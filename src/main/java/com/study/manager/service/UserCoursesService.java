package com.study.manager.service;

import static java.time.temporal.ChronoUnit.DAYS;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import com.study.manager.domain.Type;
import com.study.manager.translator.UserCoursesTranslator;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.study.manager.domain.Book;
import com.study.manager.domain.Course;
import com.study.manager.domain.CourseSettings;
import com.study.manager.domain.CourseStatus;
import com.study.manager.domain.DefaultSettingsView;
import com.study.manager.domain.Goal;
import com.study.manager.domain.Proficiency;
import com.study.manager.domain.WeeklyHours;
import com.study.manager.entity.BookEntity;
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
import com.study.manager.service.exception.ErrorCode;
import com.study.manager.service.exception.ServiceException;
import com.study.manager.translator.CourseSettingsTranslator;
import com.study.manager.translator.CourseTranslator;
import com.study.manager.translator.UserCourseBooksTranslator;
import com.study.manager.util.ServiceUtils;

@Service
@Validated
public class UserCoursesService {

	public static final int DEFAULT_BEGINNER_PAGES = 10;
	public static final int DEFAULT_NORMAL_PAGES = 15;
	public static final int DEFAULT_EXPERT_PAGES = 18;
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
	UserCoursesTranslator userCoursesTranslator;

	@Inject
	private BookRepository bookRepository;
	@Inject
	private CourseSettingsTranslator courseSettingsTranslator;
	@Inject
	private UserCourseBooksTranslator userCourseBooksTranslator;

	public void subscribeCourse(Long userId, Long courseId, LocalDate startDate) {
		UserCoursesEntity userCourseEntity = userCoursesRepository.findBy(userId, courseId);
		if (userCourseEntity != null) {
			throw new ServiceException(ErrorCode.SM_114);
		}
		userCourseEntity = new UserCoursesEntity();
		userCourseEntity.setUserId(userId);
		if(startDate == null){
			startDate = LocalDate.now();
		}
		userCourseEntity.setCourseId(courseId);
		userCourseEntity.setStartDate(startDate);
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
			userCourseBooksEntity.setImageUrl(book.getImageUrl());
			userCourseBooksEntity.setRevisionCompleted(false);
			totalNoOfPages = totalNoOfPages + book.getNoOfPages();
			userCourseBooksEntity.setCreationDateTime(LocalDateTime.now());
			userCourseBooksEntity.setLastChangeTimestamp(LocalDateTime.now());
			userCourseBooksEntities.add(userCourseBooksEntity);
		}
		userCourseEntity.setUserCourseBooksEntity(userCourseBooksEntities);
		userCourseEntity.setTotalNoOfPages(totalNoOfPages);
		userCourseEntity.setPagesUnRead(totalNoOfPages);
		userCourseEntity.setCompletionRate(0);

		CourseEntity courseEntity = courseRepository.findOne(courseId);
		userCourseEntity.setEndDate(userCourseEntity.getStartDate().plusWeeks(courseEntity.getDefaultTimeInWeeks()));
		userCourseEntity.setTitle(courseEntity.getTitle());
		userCourseEntity.setDescription(courseEntity.getDescription());
		userCourseEntity.setType(courseEntity.getType());
		userCourseEntity.setProficiency(Proficiency.EXPERT.name());
		WeeklyHoursEntity weeklyHoursEntity = new WeeklyHoursEntity();
		WeekEntity weekEntity = new WeekEntity(1, 1, 1, 1, 1, 1, 1);
		weeklyHoursEntity.setWeekEntity(weekEntity);
		weeklyHoursEntity.setCreationDateTime(LocalDateTime.now());
		weeklyHoursEntity.setLastChangeTimestamp(LocalDateTime.now());
		userCourseEntity.setWeeklyHoursEntity(weeklyHoursEntity);
		WeeklyPagesEntity weeklyPagesEntity = new WeeklyPagesEntity();
		CourseProficiencyEntity courseProficiencyEntity = courseProficiencyRepository.findByCourseId(courseId);
		int expertPages = DEFAULT_EXPERT_PAGES;
		if (courseProficiencyEntity != null) {
			expertPages = courseProficiencyEntity.getExpertPages();
		}
		WeekEntity weekEntityPages = new WeekEntity(expertPages);
		weeklyPagesEntity.setWeekEntity(weekEntityPages);
		weeklyPagesEntity.setCreationDateTime(LocalDateTime.now());
		weeklyPagesEntity.setLastChangeTimestamp(LocalDateTime.now());
		userCourseEntity.setWeeklyPagesEntity(weeklyPagesEntity);

		String currentStatus;
		if(userCourseEntity.getStartDate().isAfter(LocalDate.now())){
			currentStatus = CourseStatus.NOT_STARTED.name();
			userCourseEntity.setTodayGoal(0);
		} else {
			currentStatus = CourseStatus.ON_TRACK.name();
			userCourseEntity.setTodayGoal(weekEntityPages.getTodayGoal(LocalDate.now()));
		}
		userCourseEntity.setCurrentStatus(currentStatus);
		userCourseEntity.setDefaultSettingsView(DefaultSettingsView.DIFFICULTY.name());
		userCourseEntity.setCreationDateTime(LocalDateTime.now());
		userCourseEntity.setLastChangeTimestamp(LocalDateTime.now());
		userCoursesRepository.save(userCourseEntity);
	}

	public void unSubscribeCourse(Long userId, Long courseId) {
		UserCoursesEntity userCourseEntity = userCoursesRepository.findBy(userId, courseId);
		if(Type.CUSTOM.equals(userCourseEntity.getType())){
			courseRepository.delete(userCourseEntity.getCourseId());
			courseProficiencyRepository.delete(courseProficiencyRepository.findByCourseId(userCourseEntity.getCourseId()));
		}
		userCoursesRepository.delete(userCourseEntity);
	}

	public List<Course> getSubscribedCourses(Long userId) {
		List<UserCoursesEntity> userCoursesEntities = userCoursesRepository.findAllByUserId(userId);
		return userCoursesTranslator.translateToDomain(userCoursesEntities);
	}

	public void addCustomCourse(Long userId, Course course) {
		if (course.getBookList() == null || course.getBookList().isEmpty()) {
			throw new ServiceException(ErrorCode.SM_101);
		}
		List<UserCourseBooksEntity> userCourseBooksEntityList = new ArrayList<>();
		int totalNoOfPages = 0;
		for (Book book : course.getBookList()) {
			totalNoOfPages += book.getNoOfPages();
			userCourseBooksEntityList.add(userCourseBooksTranslator.translateToEntity(book));
		}
		CourseEntity courseEntity = courseTranslator.translateToEntity(course);
		double defaultTimeInWeeks = ServiceUtils.getDefaultCoursePreparationTimeDouble(totalNoOfPages, 18, 7);
		courseEntity.setDefaultTimeInWeeks((int) Math.ceil(defaultTimeInWeeks));
		courseEntity.setDefaultTimeInMonths((defaultTimeInWeeks * 7) / 30);
		courseEntity.setCreationDateTime(LocalDateTime.now());
		courseEntity.setLastChangeTimestamp(LocalDateTime.now());
		CourseEntity persistedEntity = courseRepository.save(courseEntity);
		UserCoursesEntity userCoursesEntity = new UserCoursesEntity(userId, persistedEntity.getId());
		userCoursesEntity.setCreationDateTime(LocalDateTime.now());
		userCoursesEntity.setTitle(persistedEntity.getTitle());
		userCoursesEntity.setDescription(persistedEntity.getDescription());
		userCoursesEntity.setType(persistedEntity.getType());
		userCoursesEntity.setDefaultSettingsView(DefaultSettingsView.DIFFICULTY.name());
		userCoursesEntity.setUserCourseBooksEntity(userCourseBooksEntityList);
		userCoursesEntity.setTotalNoOfPages(totalNoOfPages);
		userCoursesEntity.setPagesUnRead(totalNoOfPages);
		userCoursesEntity.setCompletionRate(0);
		String currentStatus = course.getStartDate().isAfter(LocalDate.now()) ? CourseStatus.NOT_STARTED.name()
				: CourseStatus.ON_TRACK.name();
		userCoursesEntity.setCurrentStatus(currentStatus);
		userCoursesEntity.setStartDate(course.getStartDate());
		int defaultTimeInDays = (int) Math.ceil((defaultTimeInWeeks * 7));
		userCoursesEntity.setEndDate(course.getStartDate().plusDays(defaultTimeInDays));
		userCoursesEntity.setProficiency(Proficiency.EXPERT.name());
		WeeklyHoursEntity weeklyHoursEntity = new WeeklyHoursEntity();
		WeekEntity weekEntity = new WeekEntity(1, 1, 1, 1, 1, 1, 1);
		weeklyHoursEntity.setWeekEntity(weekEntity);
		weeklyHoursEntity.setCreationDateTime(LocalDateTime.now());
		weeklyHoursEntity.setLastChangeTimestamp(LocalDateTime.now());
		userCoursesEntity.setWeeklyHoursEntity(weeklyHoursEntity);
		WeeklyPagesEntity weeklyPagesEntity = new WeeklyPagesEntity();
		WeekEntity weekEntityPages = new WeekEntity(DEFAULT_EXPERT_PAGES);
		weeklyPagesEntity.setCreationDateTime(LocalDateTime.now());
		weeklyPagesEntity.setLastChangeTimestamp(LocalDateTime.now());
		weeklyPagesEntity.setWeekEntity(weekEntityPages);
		userCoursesEntity.setWeeklyPagesEntity(weeklyPagesEntity);
		int todayGoal = course.getStartDate().isAfter(LocalDate.now()) ? 0
				: weekEntityPages.getTodayGoal(LocalDate.now());
		userCoursesEntity.setTodayGoal(todayGoal);
		userCoursesEntity.setLastChangeTimestamp(LocalDateTime.now());
		CourseProficiencyEntity courseProficiencyEntity = new CourseProficiencyEntity();
		courseProficiencyEntity.setCourseId(persistedEntity.getId());
		courseProficiencyEntity.setExpertPages(DEFAULT_EXPERT_PAGES);
		courseProficiencyEntity.setNormalPages(DEFAULT_NORMAL_PAGES);
		courseProficiencyEntity.setBeginnerPages(DEFAULT_BEGINNER_PAGES);
		courseProficiencyRepository.save(courseProficiencyEntity);
		userCoursesRepository.save(userCoursesEntity);

	}

	public void addCustomBook(Long userId, Long courseId, Book book) {
		UserCourseBooksEntity userCourseBooksEntity = userCourseBooksTranslator.translateToEntity(book);
		UserCoursesEntity userCoursesEntity = userCoursesRepository.findBy(userId, courseId);
		userCoursesEntity
				.setTotalNoOfPages(userCoursesEntity.getTotalNoOfPages() + userCourseBooksEntity.getTotalNoOfPages());
		userCoursesEntity
				.setPagesUnRead(userCoursesEntity.getPagesUnRead() + userCourseBooksEntity.getTotalNoOfPages());
		int totalPagesPerWeek = userCoursesEntity.getWeeklyPagesEntity().getWeekEntity().getTotalWeekCount();
		int weeksToComplete = userCourseBooksEntity.getTotalNoOfPages() / totalPagesPerWeek;
		long noOfAdditionalDays = (long) Math.ceil(weeksToComplete) * 7;
		userCoursesEntity.setEndDate(getAbsoluteEndDate(userCoursesEntity.getEndDate().plusDays(noOfAdditionalDays),
				userCoursesEntity.getWeeklyHoursEntity().getWeekEntity()));
		userCoursesEntity.getUserCourseBooksEntity().add(userCourseBooksEntity);
		userCoursesEntity.setCreationDateTime(LocalDateTime.now());
		userCoursesEntity.setLastChangeTimestamp(LocalDateTime.now());
		userCoursesRepository.save(userCoursesEntity);

	}

	public void deleteBook(Long userId, Long courseId, Long bookId) {
		UserCoursesEntity userCoursesEntity = userCoursesRepository.findBy(userId, courseId);
		UserCourseBooksEntity deleteBookEntity = userCoursesEntity.getUserCourseBooksEntity().stream()
				.filter(obj -> obj.getId().equals(bookId)).findFirst().get();
		if (deleteBookEntity == null) {
			throw new ServiceException(ErrorCode.SM_102);
		}
		userCoursesEntity.getUserCourseBooksEntity().removeIf(obj -> obj.getId().equals(bookId));
		userCoursesEntity
				.setTotalNoOfPages(userCoursesEntity.getTotalNoOfPages() - deleteBookEntity.getTotalNoOfPages());
		userCoursesEntity.setPagesRead(userCoursesEntity.getPagesRead() - deleteBookEntity.getNoOfPagesRead());
		userCoursesEntity.setPagesUnRead(userCoursesEntity.getTotalNoOfPages() - userCoursesEntity.getPagesRead());
		double completionRate = ((double) userCoursesEntity.getPagesRead() / userCoursesEntity.getTotalNoOfPages())
				* 70;
		userCoursesEntity.setCompletionRate(completionRate);
		int totalPagesPerWeek = userCoursesEntity.getWeeklyPagesEntity().getWeekEntity().getTotalWeekCount();
		double weeksToComplete = userCoursesEntity.getPagesUnRead() / totalPagesPerWeek;
		long daysToComplete = (int) Math.ceil(weeksToComplete) * 7;
		LocalDate newEndDate = userCoursesEntity.getStartDate().plusDays(daysToComplete);
		userCoursesEntity.setEndDate(newEndDate);
		userCoursesEntity.setLastChangeTimestamp(LocalDateTime.now());
		userCoursesRepository.save(userCoursesEntity);
	}

	public boolean isSubscribed(long courseId, long userId) {
		Long count = userCoursesRepository.findCount(userId, courseId);
		return count == 0 ? false : true;
	}

	public CourseSettings getSubscribedCourseSettings(long userId, Long courseId) {
		UserCoursesEntity userCoursesEntity = userCoursesRepository.findBy(userId, courseId);
		if (userCoursesEntity == null) {
			throw new ServiceException(ErrorCode.SM_100);
		}
		CourseProficiencyEntity courseProficiencyEntity = courseProficiencyRepository.findByCourseId(courseId);
		return courseSettingsTranslator.translateToDomain(userCoursesEntity, courseProficiencyEntity);
	}

	public void updateSubscribedCourseGoal(long userId, Long courseId, Goal goal) {
		UserCoursesEntity userCoursesEntity = userCoursesRepository.findBy(userId, courseId);
		List<UserCourseBooksEntity> userCourseBooksEntities = userCoursesEntity.getUserCourseBooksEntity();
		UserCourseBooksEntity userCourseBooksEntity = userCourseBooksEntities.stream()
				.filter(entry -> entry.getId().equals(goal.getBookId())).findAny().orElse(null);
		if (userCourseBooksEntity != null) {
			if (goal.isRevisionCompleted()) {
				userCourseBooksEntity.setRevisionCompleted(true);
				userCourseBooksEntity.setLastChangeTimestamp(LocalDateTime.now());
				int noOfBooks = userCourseBooksEntities.size();
				double revisionCompletedPercentage = (double) 30 / noOfBooks;
				userCoursesEntity
						.setCompletionRate(userCoursesEntity.getCompletionRate() + revisionCompletedPercentage);
				if (userCoursesEntity.getCompletionRate() >= 100.0) {
					userCoursesEntity.setCurrentStatus(CourseStatus.COMPLETED.name());
				}
			} else {
				if (CourseStatus.NOT_STARTED.name().equals(userCoursesEntity.getCurrentStatus())) {
					userCoursesEntity.setCurrentStatus(CourseStatus.ON_TRACK.name());
					userCoursesEntity.setEndDate(LocalDate.now().plusDays(
							DAYS.between(userCoursesEntity.getStartDate(), userCoursesEntity.getEndDate())));
					userCoursesEntity.setStartDate(LocalDate.now());
					int todayGoal = userCoursesEntity.getWeeklyPagesEntity().getWeekEntity()
							.getTodayGoal(LocalDate.now());
					userCoursesEntity.setTodayGoal(todayGoal);
				}
				if (userCoursesEntity.getCurrentStatus().equals(CourseStatus.BEHIND_SCHEDULE.name())
						|| userCoursesEntity.getCurrentStatus().equals(CourseStatus.ON_TRACK.name())) {
					if(goal.getNoOfPagesRead() <= userCourseBooksEntity.getNoOfPagesUnRead()) {

						userCourseBooksEntity
								.setNoOfPagesRead(userCourseBooksEntity.getNoOfPagesRead() + goal.getNoOfPagesRead());
						userCourseBooksEntity.setNoOfPagesUnRead(
								userCourseBooksEntity.getTotalNoOfPages() - userCourseBooksEntity.getNoOfPagesRead());
						userCourseBooksEntity.setLastChangeTimestamp(LocalDateTime.now());
						userCoursesEntity.setPagesRead(userCoursesEntity.getPagesRead() + goal.getNoOfPagesRead());
						userCoursesEntity
								.setPagesUnRead(userCoursesEntity.getTotalNoOfPages() - userCoursesEntity.getPagesRead());
						int todayGoal = userCoursesEntity.getTodayGoal() - goal.getNoOfPagesRead();
						userCoursesEntity.setTodayGoal(todayGoal >= 0 ? todayGoal : 0);
						if(todayGoal > 0){
							userCoursesEntity.setCurrentStatus(CourseStatus.BEHIND_SCHEDULE.name());
						} else {
							userCoursesEntity.setCurrentStatus(CourseStatus.ON_TRACK.name());
						}
						double completionRate = ((double) goal.getNoOfPagesRead()
								/ userCoursesEntity.getTotalNoOfPages()) * 70;
						userCoursesEntity.setCompletionRate(userCoursesEntity.getCompletionRate() + completionRate);
						if (userCoursesEntity.getTotalNoOfPages() <= userCoursesEntity.getPagesRead()) {
							userCoursesEntity.setCurrentStatus(CourseStatus.REVISION_PENDING.name());
						}
					} else {
						throw new ServiceException(ErrorCode.SM_117);
					}
				}
			}
		} else {
			throw new ServiceException(ErrorCode.SM_102);
		}
		userCoursesEntity.setLastChangeTimestamp(LocalDateTime.now());
		userCoursesRepository.save(userCoursesEntity);

	}

	public void updateSubscribedCourseSettings(long userId, Long courseId, CourseSettings courseSettings) {
		UserCoursesEntity userCoursesEntity = userCoursesRepository.findBy(userId, courseId);
		WeeklyHours newWeeklyHours = courseSettings.getWeeklyHours();
		LocalDate newTargetDate = courseSettings.getTargetDate();
		if (newTargetDate != null) {
			int pagesUnRead = userCoursesEntity.getPagesUnRead();
			long noOfDaysLeft = DAYS.between(LocalDate.now(), newTargetDate);
			int dailyPagesToRead = (int) (pagesUnRead / noOfDaysLeft);
			WeekEntity weekDayPages = new WeekEntity(dailyPagesToRead);
			userCoursesEntity.getWeeklyPagesEntity().setWeekEntity(weekDayPages);
			userCoursesEntity.getWeeklyPagesEntity().setLastChangeTimestamp(LocalDateTime.now());
			WeekEntity weekDayHours = new WeekEntity(0);
			userCoursesEntity.getWeeklyHoursEntity().setWeekEntity(weekDayHours);
			userCoursesEntity.getWeeklyHoursEntity().setLastChangeTimestamp(LocalDateTime.now());
			userCoursesEntity.setTodayGoal(dailyPagesToRead);
			userCoursesEntity.setEndDate(newTargetDate);
			userCoursesEntity.setDefaultSettingsView(DefaultSettingsView.TARGET_DATE.name());

		} else {
			userCoursesEntity.setDefaultSettingsView(DefaultSettingsView.DIFFICULTY.name());
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
				userCoursesEntity.setTodayGoal(weekDayPages.getTodayGoal(LocalDate.now()));
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
				userCoursesEntity.setTodayGoal(weekDayPages.getTodayGoal(LocalDate.now()));
				userCoursesEntity.setEndDate(
						getAbsoluteEndDate(newEndDate, userCoursesEntity.getWeeklyHoursEntity().getWeekEntity()));
			}
		}
		userCoursesEntity.setLastChangeTimestamp(LocalDateTime.now());
		userCoursesRepository.save(userCoursesEntity);
	}

	public void updateSubscribedStartDate(long userId, Long courseId, LocalDate startDate){
		UserCoursesEntity userCoursesEntity = userCoursesRepository.findBy(userId, courseId);
		long noOfDaysToAdd = DAYS.between(userCoursesEntity.getEndDate(), userCoursesEntity.getStartDate());
		userCoursesEntity.setStartDate(startDate);
		userCoursesEntity.setEndDate(userCoursesEntity.getStartDate().plusDays(noOfDaysToAdd));
		if(userCoursesEntity.getStartDate().isEqual(LocalDate.now())){
			userCoursesEntity.setCurrentStatus(CourseStatus.ON_TRACK.name());
			userCoursesEntity.setTodayGoal(userCoursesEntity.getWeeklyPagesEntity().getWeekEntity().getTodayGoal(LocalDate.now()));
		}
		userCoursesEntity.setLastChangeTimestamp(LocalDateTime.now());
		userCoursesRepository.save(userCoursesEntity);
	}
	public static LocalDate getAbsoluteEndDate(LocalDate endDate, WeekEntity weekDayHours) {
		DayOfWeek actualDayOfWeek = weekDayHours.getNonZeroDay();
		return endDate.with(TemporalAdjusters.next(actualDayOfWeek));
	}

	private WeekEntity getWeekDayPages(Long courseId, String proficiency, WeekEntity weekDayHours) {
		CourseProficiencyEntity courseProficiencyEntity = courseProficiencyRepository.findByCourseId(courseId);
		int pagesPerHour = 0;
		if (proficiency.equals(Proficiency.BEGINNER.name())) {
			pagesPerHour = courseProficiencyEntity.getBeginnerPages();
		} else if (proficiency.equals(Proficiency.NORMAL.name())) {
			pagesPerHour = courseProficiencyEntity.getNormalPages();
		} else if (proficiency.equals(Proficiency.EXPERT.name())) {
			pagesPerHour = courseProficiencyEntity.getExpertPages();
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
			book.setDescription(userCourseBooksEntity.getDescription());
			book.setId(userCourseBooksEntity.getId());
			book.setNoOfPagesRead(userCourseBooksEntity.getNoOfPagesRead());
			book.setNoOfPagesUnRead(userCourseBooksEntity.getNoOfPagesUnRead());
			book.setImageUrl(userCourseBooksEntity.getImageUrl());
			book.setRevisionCompleted(userCourseBooksEntity.isRevisionCompleted());
			bookList.add(book);
		}
		course.setTodayGoal(userCoursesEntity.getTodayGoal());
		course.setBookList(bookList);
		course.setStartDate(userCoursesEntity.getStartDate());
		course.setEndDate(userCoursesEntity.getEndDate());
		course.setCurrentStatus(userCoursesEntity.getCurrentStatus());
		return course;

	}

	public void addDailyGoal() {
		List<UserCoursesEntity> userCourses = userCoursesRepository.findAll();
		if (userCourses != null && !userCourses.isEmpty()) {
			for (UserCoursesEntity userCoursesEntity : userCourses) {
				LocalDate startDate = userCoursesEntity.getStartDate();
				boolean update = false;
				if (startDate.equals(LocalDate.now())) {
					userCoursesEntity.setCurrentStatus(CourseStatus.ON_TRACK.name());
					update = true;
				}
				String currentStatus = userCoursesEntity.getCurrentStatus();
				if (currentStatus.equals(CourseStatus.ON_TRACK.name()) ||
						currentStatus.equals(CourseStatus.BEHIND_SCHEDULE.name())) {
					int todayGoal = userCoursesEntity.getWeeklyPagesEntity().getWeekEntity()
							.getTodayGoal(LocalDate.now());
					int pastGoal = userCoursesEntity.getTodayGoal();
					if (pastGoal > 0) {
						userCoursesEntity.setCurrentStatus(CourseStatus.BEHIND_SCHEDULE.name());
					} else {
						userCoursesEntity.setCurrentStatus(CourseStatus.ON_TRACK.name());
					}
					todayGoal = todayGoal + pastGoal;
					userCoursesEntity.setTodayGoal(todayGoal > userCoursesEntity.getPagesUnRead()
							? userCoursesEntity.getPagesUnRead() : todayGoal);
					update = true;
				}
				if (update) {
					userCoursesRepository.save(userCoursesEntity);

				}
			}
		}
	}
	public void addDailyGoalForUser(Long userId) {
		List<UserCoursesEntity> userCourses = userCoursesRepository.findAllByUserId(userId);
		if (userCourses != null && !userCourses.isEmpty()) {
			for (UserCoursesEntity userCoursesEntity : userCourses) {
				LocalDate startDate = userCoursesEntity.getStartDate();
				boolean update = false;
				if (startDate.equals(LocalDate.now())) {
					userCoursesEntity.setCurrentStatus(CourseStatus.ON_TRACK.name());
					update = true;
				}
				String currentStatus = userCoursesEntity.getCurrentStatus();
				if (currentStatus.equals(CourseStatus.ON_TRACK.name()) ||
						currentStatus.equals(CourseStatus.BEHIND_SCHEDULE.name())) {
					int todayGoal = userCoursesEntity.getWeeklyPagesEntity().getWeekEntity()
							.getTodayGoal(LocalDate.now());
					int pastGoal = userCoursesEntity.getTodayGoal();
					if (pastGoal > 0) {
						userCoursesEntity.setCurrentStatus(CourseStatus.BEHIND_SCHEDULE.name());
					} else {
						userCoursesEntity.setCurrentStatus(CourseStatus.ON_TRACK.name());
					}
					todayGoal = todayGoal + pastGoal;
					userCoursesEntity.setTodayGoal(todayGoal > userCoursesEntity.getPagesUnRead()
							? userCoursesEntity.getPagesUnRead() : todayGoal);
					update = true;
				}
				if (update) {
					userCoursesRepository.save(userCoursesEntity);

				}
			}
		}
	}

	public void updatePriorityForSubscribedCourses(Long userId, int from, int to) {
		List<UserCoursesEntity> userCoursesEntities = userCoursesRepository.findAllByUserId(userId);
		if(from > to){
			Collections.rotate(userCoursesEntities.subList(to - 1, from), 1);
		}
		int i= 1;
		for(UserCoursesEntity userCoursesEntity : userCoursesEntities){
			userCoursesEntity.setPriority(i++);
		}
		userCoursesRepository.save(userCoursesEntities);
	}
	
	public static void main(String ap[]){
		List<UserCoursesEntity> userCoursesEntities = new ArrayList<>();
		add(userCoursesEntities, 1);
		add(userCoursesEntities, 2);
		add(userCoursesEntities, 3);
		add(userCoursesEntities, 4);
		add(userCoursesEntities, 5);
		userCoursesEntities.forEach(item->System.out.println("Before rotate : id : "+item.getId() +" prio : "+item.getPriority()));
		rotate(userCoursesEntities, 5, 2);
		userCoursesEntities.forEach(item->System.out.println("After rotate : id : "+item.getId() +" prio : "+item.getPriority()));
		
	}
	
	public static List<UserCoursesEntity> rotate(List<UserCoursesEntity> userCoursesEntities, int from, int to){
		if(from > to){
			Collections.rotate(userCoursesEntities.subList(to - 1, from), 1);
		} else {
			Collections.rotate(userCoursesEntities.subList(to , from), 1);
		}
		int i= 1;
		for(UserCoursesEntity userCoursesEntity : userCoursesEntities){
			userCoursesEntity.setPriority(i++);
		}
		return userCoursesEntities;
		
	}
	public static void add(List<UserCoursesEntity> userCoursesEntities, int id){
		
		UserCoursesEntity coursesEntity = new UserCoursesEntity();
		coursesEntity.setId(Long.valueOf(id));
		coursesEntity.setPriority(id);
		userCoursesEntities.add(coursesEntity);
		
	}
}
