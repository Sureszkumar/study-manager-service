package com.study.manager.service;

import java.time.LocalDateTime;

import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.study.manager.domain.User;
import com.study.manager.entity.UserEntity;
import com.study.manager.repository.UserRepository;
import com.study.manager.security.Password;
import com.study.manager.translator.UserTranslator;
import com.study.manager.util.EmailService;
import com.study.manager.util.ServiceUtils;
import com.study.manager.validator.CredentialsValidator;

@Service
@Validated
public class UserService {

	@Inject
	private UserRepository userRepository;

	@Inject
	private UserTranslator userTranslator;

	@Inject
	private CredentialsValidator credentialsValidator;
	
	@Inject
	private EmailService emailService;

	@Transactional
	public User create(User user) {

		boolean isValidCredentials = credentialsValidator.validate(user.getEmail(), user.getPassword());
		if (!isValidCredentials) {
			throw new IllegalArgumentException();
		}
		if (userRepository.findByEmail(user.getEmail()) != null) {
			throw new EntityExistsException();
		}
		
		UserEntity userEntity = userTranslator.translateToEntity(user);
		try {
			userEntity.setPassword(Password.getSaltedHash(userEntity.getPassword()));
			userEntity.setAuthToken(ServiceUtils.createAuthToken(userEntity.getEmail()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LocalDateTime now = LocalDateTime.now();
		userEntity.setLastChangeTimestamp(now);
		userEntity.setCreationDateTime(now);
		userRepository.save(userEntity);
		boolean emailSent = emailService.sendEmail(userEntity.getId(), userEntity.getEmail());
		user = userTranslator.translateToDomain(userEntity);
		user.setEmailSent(emailSent);
		return user;
	}
	
	@Transactional
	public User loginUser(User user) {

		boolean isValidCredentials = credentialsValidator.validate(user.getEmail(), user.getPassword());
		if (!isValidCredentials) {
			throw new IllegalArgumentException();
		}
		UserEntity userEntity = null;
		try {
			userEntity = userRepository.findByCredentials(user.getEmail(), Password.getSaltedHash(user.getPassword()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if ( userEntity == null) {
			throw new EntityNotFoundException();
		}
		return userTranslator.translateToDomain(userEntity);
	}
	
	public User get(Long id) {
		UserEntity userEntity = userRepository.findOne(id);
		if(userEntity == null) {
			throw new EntityNotFoundException();
		}
		return userTranslator.translateToDomain(userEntity);
	}

	public User findUserByEmail(String email) {
		UserEntity userEntity = userRepository.findByEmail(email);
		if(userEntity == null) {
			throw new EntityNotFoundException();
		}
		return userTranslator.translateToDomain(userEntity);
	}

	public UserEntity findByUserIdAndToken(Long id, String token){
		return userRepository.findByUserIdAndToken(id, token);
	}
}
