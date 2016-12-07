package com.study.manager.util;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.study.manager.domain.Course;
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
		String pwd = RandomStringUtils.random(10, 0, 62, false, false, characters.toCharArray(), new SecureRandom());
		return pwd;
	}

	public static int getDefaultCoursePreparationTime(int noOfPages, int noOfPagesPerHour, int noOfHoursPerWeek) {

		return noOfPages / (noOfPagesPerHour * noOfHoursPerWeek);

	}
	
	public static double getDefaultCoursePreparationTimeDouble(int noOfPages, int noOfPagesPerHour, int noOfHoursPerWeek) {

		double multiplyExact = Math.multiplyExact(noOfPagesPerHour, noOfHoursPerWeek);
		double result = noOfPages / multiplyExact;
		return result;

	}
	public static String formVerifyEmailUrl(Long id, String email) {
		return "";

	}

	public static void main(String a[]){
		List<Course> courseList = new ArrayList<>();
		Course course = new Course();
		course.setId(Long.valueOf(1));
		course.setPriority(1);
		courseList.add(course);

		Course course1 = new Course();
		course1.setId(Long.valueOf(2));
		course1.setPriority(2);
		courseList.add(course1);

		Course course2 = new Course();
		course2.setId(Long.valueOf(3));
		course2.setPriority(3);
		courseList.add(course2);

		Course course3 = new Course();
		course3.setId(Long.valueOf(4));
		course3.setPriority(4);
		courseList.add(course3);

		Course course4 = new Course();
		course4.setId(Long.valueOf(5));
		course4.setPriority(5);
		courseList.add(course4);
		//courseList.forEach(item -> System.out.println("Original : "+item.getId()+" Priority : "+item.getPriority()));

		//Collections.swap(courseList, 4, 0);
		/*int i = 1;
		for(Course cours : courseList){

			cours.setPriority(i++);
		}*/
		//Collections.rotate(courseList, 1);
		int i = 1;
		for(Course cours : courseList){

			cours.setPriority(i++);
		}
		updatePriority(courseList, 3, 2);

		courseList.forEach(item -> System.out.println("after swap :  "+item.getId()+" Priority : "+item.getPriority()));
	}

	private static void updatePriority(List<Course> courseList, int from, int to) {

		for(int i = to-1; i< from ; i++){
			System.out.println(" id : "+courseList.get(i).getId());
		}

	}
}
