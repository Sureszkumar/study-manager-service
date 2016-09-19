package com.study.manager.translator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.study.manager.domain.Course;
import com.study.manager.entity.CourseEntity;

@Component
public class CourseTranslator {

	public List<Course> translateToDomain(List<CourseEntity> courseEntities) {

		List<Course> courseList = new ArrayList<>();
		for (CourseEntity courseEntity : courseEntities) {
			Course course = new Course();
			course.setId(courseEntity.getId());
			course.setTitle(courseEntity.getTitle());
			courseList.add(course);
		}
		return courseList;
	}
	
	public CourseEntity translateToEntity(Course course) {
		CourseEntity courseEntity = new CourseEntity();
		courseEntity.setTitle(course.getTitle());
		return courseEntity;

	}

}
