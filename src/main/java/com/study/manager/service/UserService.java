package com.study.manager.service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.study.manager.entity.UserEntity;
import com.study.manager.repository.UserRepository;
import com.study.manager.security.Password;
import com.study.manager.util.ServiceUtils;
import com.study.manager.validator.CredentialsValidator;

@Service
@Validated
public class UserService extends BaseService<UserEntity> {

	@Inject
	private UserRepository userRepository;
	
	@Inject
	private CredentialsValidator credentialsValidator;

	@PostConstruct
	protected void init() {
		super.init(userRepository);
	}

	@Override
	public UserEntity create(UserEntity userEntity) {

		boolean isValidCredentials = credentialsValidator.validate(userEntity.getEmail(), userEntity.getPassword());
		if(!isValidCredentials){
			throw new IllegalArgumentException();
		}
		try {
			userEntity.setPassword(Password.getSaltedHash(userEntity.getPassword()));
			userEntity.setAuthToken(ServiceUtils.createAuthToken(userEntity.getEmail()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return super.create(userEntity);
	}

}
