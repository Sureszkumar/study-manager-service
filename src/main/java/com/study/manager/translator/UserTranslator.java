package com.study.manager.translator;

import org.springframework.stereotype.Component;

import com.study.manager.domain.User;
import com.study.manager.entity.UserEntity;

@Component
public class UserTranslator {

	public User translateToDomain(UserEntity userEntity) {
		User user = new User();
		user.setId(userEntity.getId());
		user.setEmail(userEntity.getEmail());
		user.setAuthToken(userEntity.getAuthToken());
		return user;
	}

	public UserEntity translateToEntity(User user) {
		UserEntity userEntity = new UserEntity();
		userEntity.setEmail(user.getEmail());
		userEntity.setPassword(user.getPassword());
		return userEntity;

	}

}
