package com.study.manager.repository;

import com.study.manager.entity.UserEntity;

public interface UserRepository extends BaseRepository<UserEntity> {
	
	
	public UserEntity findByEmail(String email);
	
	public UserEntity findByCredentials(String email, String password);

	public UserEntity findByUserIdAndAuthToken(Long valueOf, String authToken);

	public UserEntity findByUserIdAndEmailToken(Long id, String emailVerifyToken);

	public void verifyUser(Long userId);
	
}
