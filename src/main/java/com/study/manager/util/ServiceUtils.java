package com.study.manager.util;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import com.study.manager.domain.Proficiency;

public class ServiceUtils {

	public static String createAuthToken(String userEmailChunk) {
		String token = UUID.randomUUID().toString().toUpperCase() + Base64.encodeBase64String(userEmailChunk.getBytes())
				+ LocalDateTime.now().get(ChronoField.MILLI_OF_SECOND);
		return token;
	}

	public static String decryptUserId(String userId) {
		byte[] valueDecoded = Base64.decodeBase64(userId.getBytes());
		return new String(valueDecoded);
	}

	public static String encrypttUserId(String userId) {
		byte[] bytesEncoded = Base64.encodeBase64(userId.getBytes());
		return new String(bytesEncoded);
	}

	public static String createEmailVerifyToken(String userId) {
		String token = UUID.randomUUID().toString().toUpperCase() + Base64.encodeBase64String(userId.getBytes());
		return token;
	}

	public static void copyNonNullProperties(Object src, Object target) {
		BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
	}

	public static String[] getNullPropertyNames(Object source) {
		final BeanWrapper src = new BeanWrapperImpl(source);
		java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

		Set<String> emptyNames = new HashSet<>();
		for (java.beans.PropertyDescriptor pd : pds) {
			Object srcValue = src.getPropertyValue(pd.getName());
			if (srcValue == null)
				emptyNames.add(pd.getName());
		}
		String[] result = new String[emptyNames.size()];
		return emptyNames.toArray(result);
	}

	public static LocalDate calculateTargetDate(LocalDate startDate, Long noOfPages, Proficiency proficiency,
			Long noOfHours) {

		return null;
	}

	public static String generateRandomPassword(){
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		String pwd = RandomStringUtils.random(10, 0, 0, false, false, characters.toCharArray(), new SecureRandom());
		return pwd;
	}


	public static int getDefaultCoursePreparationTime(int noOfPages, int noOfPagesPerHour, int noOfHoursPerWeek) {

		return noOfPages / (noOfPagesPerHour * noOfHoursPerWeek);

	}
	public static String formVerifyEmailUrl(Long id, String email) {
		return "";

	}
}
