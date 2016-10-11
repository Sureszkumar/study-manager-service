package com.study.manager.entity;

import com.study.manager.domain.Type;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "COURSE")
public class CourseEntity extends BaseEntity {
	
	private String title;

	private String description;
	
	@Enumerated(EnumType.STRING)
	private Type type;
	
	private int defaultTimeInWeeks;

	public int getDefaultTimeInWeeks() {
		return defaultTimeInWeeks;
	}

	public void setDefaultTimeInWeeks(int defaultTimeInWeeks) {
		this.defaultTimeInWeeks = defaultTimeInWeeks;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
}
