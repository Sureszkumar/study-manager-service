package com.study.manager.repository;

import com.study.manager.entity.UserCoursesEntity;

import java.util.List;

public interface UserCoursesRepository extends BaseRepository<UserCoursesEntity> {

	List<Long> findAllCourses(Long userId);

	Long findCount(long userId, long courseId);
}
