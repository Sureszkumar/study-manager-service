package com.study.manager.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.study.manager.domain.CourseType;

@Entity
@Table(name = "COURSE")
public class CourseEntity extends BaseEntity {
	
	private String title;

	private String description;
	
	@Enumerated(EnumType.STRING)
	private CourseType type;
	
	private int defaultTime;
	
	public int getDefaultTime() {
		return defaultTime;
	}

	public void setDefaultTime(int defaultTime) {
		this.defaultTime = defaultTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public CourseType getType() {
		return type;
	}

	public void setType(CourseType type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
}
