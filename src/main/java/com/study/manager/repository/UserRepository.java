package com.study.manager.repository;

import com.study.manager.entity.UserEntity;
import org.springframework.cache.annotation.Cacheable;

public interface UserRepository extends BaseRepository<UserEntity> {

	@Cacheable(value = "findByEmail")
	public UserEntity findByEmail(String email);
	
	public UserEntity findByCredentials(String email, String password);

	@Cacheable(value = "findByUserIdAndAuthToken")
	public UserEntity findByUserIdAndAuthToken(Long valueOf, String authToken);

	@Cacheable(value = "findByUserIdAndEmailToken")
	public UserEntity findByUserIdAndEmailToken(Long id, String emailVerifyToken);

	public void verifyUser(Long userId);
	
}
