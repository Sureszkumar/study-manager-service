package com.study.manager.translator;

import org.springframework.stereotype.Component;

import com.study.manager.domain.User;
import com.study.manager.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserTranslator {

	public User translateToDomain(UserEntity userEntity) {
		User user = new User();
		user.setId(userEntity.getId());
		user.setEmail(userEntity.getEmail());
		user.setName(userEntity.getName());
		user.setAuthToken(userEntity.getAuthToken());
		return user;
	}

	public List<User> translateToDomain(List<UserEntity> userEntities) {

		List<User> users = new ArrayList<>();
		for(UserEntity userEntity :userEntities){
			users.add(translateToDomain(userEntity));
		}
		return users;
	}

	public UserEntity translateToEntity(User user) {
		UserEntity userEntity = new UserEntity();
		userEntity.setEmail(user.getEmail());
		userEntity.setPassword(user.getPassword());
		return userEntity;

	}

}
