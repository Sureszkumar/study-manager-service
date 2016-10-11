package com.study.manager.entity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "WEEKLY_HOURS")
public class WeeklyHoursEntity extends BaseEntity {

	@Embedded
	private WeekEntity weekEntity;

	public WeekEntity getWeekEntity() {
		return weekEntity;
	}

	public void setWeekEntity(WeekEntity weekEntity) {
		this.weekEntity = weekEntity;
	}

	
}
