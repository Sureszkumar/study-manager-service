package com.study.manager.entity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "PROFICIENCY")
public class ProficiencyEntity extends BaseEntity {

	private String value;

	private UserCoursesEntity userCoursesEntity;

	@OneToOne(mappedBy = "weeklyHoursEntity")
	public UserCoursesEntity getUserCoursesEntity() {
		return userCoursesEntity;
	}

	public void setUserCoursesEntity(UserCoursesEntity userCoursesEntity) {
		this.userCoursesEntity = userCoursesEntity;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}