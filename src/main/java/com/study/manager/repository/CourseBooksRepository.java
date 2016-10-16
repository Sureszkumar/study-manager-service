package com.study.manager.repository;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;

import com.study.manager.entity.CourseBooksEntity;

public interface CourseBooksRepository extends BaseRepository<CourseBooksEntity> {

    List<Long> findBookIds(Long courseId);
}
