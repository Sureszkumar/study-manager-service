package com.study.manager.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "course")
public class CourseEntity extends BaseEntity {
	
	private String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
}
