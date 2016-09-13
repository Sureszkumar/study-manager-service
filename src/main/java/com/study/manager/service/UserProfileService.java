package com.study.manager.service;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;

import com.study.manager.domain.User;
import com.study.manager.repository.UserRepository;
import com.study.manager.validator.CredentialsValidator;

public class UserProfileService {

	@Inject
	private UserRepository userRepository;
	
	@Inject
	private CredentialsValidator credentialsValidator;
	
	/*public ResponseEntity<?> createUser(User user) {
		boolean isValidCredentials = credentialsValidator.validate(user.getEmail(), user.getPassword());
		if(isValidCredentials){
			
		} else {
			ResponseEntity.badRequest();
		}
		
	}*/

}
