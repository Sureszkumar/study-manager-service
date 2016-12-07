package com.study.manager.repository;

import com.study.manager.entity.CourseProficiencyEntity;

public interface CourseProficiencyRepository extends BaseRepository<CourseProficiencyEntity> {

	public CourseProficiencyEntity findByCourseId(long courseId);

	public void deleteByCourseId(long courseId);
}
