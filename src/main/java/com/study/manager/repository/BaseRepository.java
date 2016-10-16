package com.study.manager.repository;


import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.study.manager.entity.BaseEntity;

public interface BaseRepository<T extends BaseEntity> extends JpaRepository<T, Long>{
	
	T findOne(Long id);

	List<T> findAll();

	<S extends T> S save(S entity);

	void delete(Long id);
}
