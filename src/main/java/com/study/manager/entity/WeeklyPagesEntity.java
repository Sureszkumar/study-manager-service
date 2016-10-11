package com.study.manager.entity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "WEEKLY_PAGES")
public class WeeklyPagesEntity extends BaseEntity {

	@Embedded
	private WeekEntity weekEntity;

	public WeekEntity getWeekEntity() {
		return weekEntity;
	}

	public void setWeekEntity(WeekEntity weekEntity) {
		this.weekEntity = weekEntity;
	}

}
