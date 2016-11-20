package com.study.manager.domain;

import java.time.LocalDate;
import java.util.List;

public class Courses {

	private List<Course> courses;

	private LocalDate lastUpdatedDate;

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	public LocalDate getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(LocalDate lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

}
