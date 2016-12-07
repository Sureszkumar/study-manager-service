package com.study.manager.translator;

import com.study.manager.domain.Book;
import com.study.manager.domain.Course;
import com.study.manager.entity.UserCourseBooksEntity;
import com.study.manager.entity.UserCoursesEntity;
import com.study.manager.repository.UserCoursesRepository;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class UserCoursesTranslator {

	@Inject
	private UserCoursesRepository userCoursesRepository;

	public List<Course> translateToDomain(List<UserCoursesEntity> userCoursesEntities) {

		List<Course> courseList = new ArrayList<>();
		for (UserCoursesEntity userCoursesEntity : userCoursesEntities) {
			Course course = new Course();
			List<UserCourseBooksEntity> userCourseBooksEntities = userCoursesEntity.getUserCourseBooksEntity();
			UserCourseBooksEntity userCourseBooksEntity = userCourseBooksEntities.stream().max(Comparator.comparing(
					UserCourseBooksEntity::getLastChangeTimestamp)).get();
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
			course.setId(userCoursesEntity.getCourseId());
			course.setTitle(userCoursesEntity.getTitle());
			course.setDescription(userCoursesEntity.getDescription());
			course.setType(userCoursesEntity.getType().name());
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

}
