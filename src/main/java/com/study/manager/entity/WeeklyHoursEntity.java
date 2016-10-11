package com.study.manager.entity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "WEEKLY_HOURS")
public class WeeklyHoursEntity extends BaseEntity {

	@Embedded
	private WeekEntity weekEntity;

	private UserCoursesEntity userCoursesEntity;

	@OneToOne(mappedBy = "weeklyHoursEntity")
	public UserCoursesEntity getUserCoursesEntity() {
		return userCoursesEntity;
	}

	public void setUserCoursesEntity(UserCoursesEntity userCoursesEntity) {
		this.userCoursesEntity = userCoursesEntity;
	}

	public WeekEntity getWeekEntity() {
		return weekEntity;
	}

	public void setWeekEntity(WeekEntity weekEntity) {
		this.weekEntity = weekEntity;
	}

	
}
