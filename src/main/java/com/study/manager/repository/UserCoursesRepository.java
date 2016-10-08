package com.study.manager.repository;

import com.study.manager.entity.UserCoursesEntity;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;

public interface UserCoursesRepository extends BaseRepository<UserCoursesEntity> {

	@Cacheable("userCourseIds")
	List<Long> findAllCourses(Long userId);

	@Cacheable("findCount")
	Long findCount(long userId, long courseId);

	@Cacheable("userCourse")
	UserCoursesEntity findBy(Long userId, Long courseId);
}
