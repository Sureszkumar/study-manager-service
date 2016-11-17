package com.study.manager.translator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

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
			course.setId(courseId);
			course.setTitle(courseEntity.getTitle());
			course.setDescription(courseEntity.getDescription());
			course.setType(courseEntity.getType().name());
			course.setStartDate(userCoursesEntity.getStartDate());
			course.setEndDate(userCoursesEntity.getEndDate());
			course.setCurrentStatus(userCoursesEntity.getCurrentStatus());
			course.setCompletionRate(BigDecimal.valueOf(userCoursesEntity.getCompletionRate()));
			course.setTodayGoal(userCoursesEntity.getTodayGoal());
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
