package com.study.manager.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import com.study.manager.entity.UserCoursesEntity;
import com.study.manager.repository.UserCoursesRepository;
import com.study.manager.util.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.study.manager.domain.User;
import com.study.manager.entity.UserEntity;
import com.study.manager.repository.UserRepository;
import com.study.manager.security.Password;
import com.study.manager.service.exception.CredentialsException;
import com.study.manager.service.exception.EmailVerificationException;
import com.study.manager.service.exception.ServiceException;
import com.study.manager.translator.UserTranslator;
import com.study.manager.util.ServiceUtils;
import com.study.manager.validator.CredentialsValidator;

@Service
@Validated
public class UserService {

	private Logger log = LoggerFactory.getLogger(UserService.class);
	
	@Inject
	private UserRepository userRepository;

	@Inject
	private UserCoursesRepository userCoursesRepository;

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
			throw new IllegalArgumentException("Invalid email or username");
		}
		log.debug("User credentials valid {}", isValidCredentials);
		if (userRepository.findByEmail(user.getEmail()) != null) {
			throw new EntityExistsException("Email already exists");
		}

		UserEntity userEntity = userTranslator.translateToEntity(user);
		try {
			userEntity.setPassword(Password.encrypt(userEntity.getPassword()));
			userEntity.setAuthToken(ServiceUtils.createAuthToken(userEntity.getEmail()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LocalDateTime now = LocalDateTime.now();
		userEntity.setLastChangeTimestamp(now);
		userEntity.setCreationDateTime(now);
		userRepository.save(userEntity);
		user = userTranslator.translateToDomain(userEntity);
		return user;
	}

	@Transactional
	public User loginUser(User user) {

		boolean isValidCredentials = credentialsValidator.validate(user.getEmail(), user.getPassword());
		if (!isValidCredentials) {
			throw new CredentialsException("Invalid email id");
		}
		log.debug("User credentials valid {}", isValidCredentials);
		UserEntity userEntity = null;
		try {
			userEntity = userRepository.findByEmail(user.getEmail());
		} catch (Exception e) {
			throw new ServiceException("Exception while finding user");

		}
		if (userEntity == null) {
			throw new CredentialsException("User not found for email" + user.getEmail());
		}
		if (!userEntity.getVerified()) {
			throw new EmailVerificationException("Email not verified");
		}
		try {
			if (!Password.check(user.getPassword(), userEntity.getPassword())) {
				throw new CredentialsException("Invalid valid password");
			}
		} catch (Exception e) {
			throw new ServiceException("Exception while decrypting password");
		}
		return userTranslator.translateToDomain(userEntity);
	}

	public User get(Long id) {
		UserEntity userEntity = userRepository.findOne(id);
		if (userEntity == null) {
			throw new EntityNotFoundException();
		}
		return userTranslator.translateToDomain(userEntity);
	}

	public User findUserByEmail(String email) {
		UserEntity userEntity = userRepository.findByEmail(email);
		if (userEntity == null) {
			throw new EntityNotFoundException();
		}
		return userTranslator.translateToDomain(userEntity);
	}

	public void verifyEmail(String encryptedUserId, String token) {
		String userId = ServiceUtils.decryptUserId(encryptedUserId);
		UserEntity userEntity = userRepository.findByUserIdAndEmailToken(Long.valueOf(userId), token);
		if (userEntity == null) {
			throw new EmailVerificationException("Invalid email verify token for user");
		}
		userEntity.setVerified(true);
		userRepository.save(userEntity);
	}

	public UserEntity findByUserIdAndAuthToken(Long id, String token) {
		return userRepository.findByUserIdAndAuthToken(id, token);
	}

	@Transactional
	public UserEntity updateEmailVerifyToken(Long id, String emailVerifyToken) {
		UserEntity existing = userRepository.findOne(id);
		if (existing == null) {
			throw new ServiceException(String.format("user with id =%s not exist", id));
		}
		// copyNonNullProperties(o, existing);
		existing.setLastChangeTimestamp(LocalDateTime.now());
		existing.setEmailVerifyToken(emailVerifyToken);
		return userRepository.save(existing);
	}
	
	public void delete(Long userId) {
		userRepository.delete(userId);
		List<UserCoursesEntity> userCoursesEntities = userCoursesRepository.findAllByUserId(userId);
		userCoursesRepository.delete(userCoursesEntities);
	}

	public UserEntity updateUser(Long userId, UserEntity newUserEntity){
        UserEntity existing = userRepository.findOne(userId);
        if (existing == null) {
            throw new ServiceException(
                    String.format("user with id =%s not exist", userId));
        }
        ServiceUtils.copyNonNullProperties(newUserEntity, existing);
        existing.setLastChangeTimestamp(LocalDateTime.now());
        return userRepository.save(existing);
	}
	public void verifyUser(Long userId) {
		UserEntity existing = userRepository.findOne(userId);
		existing.setVerified(true);
		existing.setLastChangeTimestamp(LocalDateTime.now());
		userRepository.save(existing);
	}

	public List<User> findAll() {
		return userTranslator.translateToDomain(userRepository.findAll());

	}

    public void sendPassword(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) {
            throw new CredentialsException("User not found for email" + email);
        }
        try {
            String randomPassword = ServiceUtils.generateRandomPassword();
            userEntity.setPassword(Password.encrypt(randomPassword));
            userRepository.save(userEntity);
            emailService.sendNewPassword(email, randomPassword);
        } catch (Exception e) {
            throw new ServiceException("Exception while encrypting password");
        }

    }

    public void resetPassword(Long userId, String password) {
        UserEntity userEntity = userRepository.findOne(userId);
        if (userEntity == null) {
            throw new ServiceException("User not found for userId" + userId);
        }
        try {
            userEntity.setPassword(Password.encrypt(password));
            userRepository.save(userEntity);
        } catch (Exception e) {
            throw new ServiceException("Exception while encrypting password");
        }
    }

    public void updateUserProfile(Long userId, User user) {
        UserEntity userEntity = userRepository.findOne(userId);
        if (userEntity == null) {
            throw new ServiceException("User not found for userId" + userId);
        }
        userEntity.setName(user.getName());
        userRepository.save(userEntity);
    }
}
