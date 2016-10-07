package com.study.manager.repository;


import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.study.manager.entity.BaseEntity;

public interface BaseRepository<T extends BaseEntity> extends JpaRepository<T, Long>{
	
	@Cacheable(value = "findOne")
	T findOne(Long id);

	@Cacheable(value = "findAll")
	List<T> findAll();

	@CacheEvict(value = "save", allEntries = true)
	<S extends T> S save(S entity);

	@CacheEvict(value = "delete", allEntries = true)
	void delete(Long id);
}
