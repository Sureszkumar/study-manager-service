package com.study.manager.repository;

import com.study.manager.entity.BookEntity;
import com.study.manager.entity.CourseBooksEntity;
import com.study.manager.entity.UserCoursesEntity;

import java.util.List;

public interface CourseBooksRepository extends BaseRepository<CourseBooksEntity> {

    List<Long> findBookIds(Long courseId);
}
