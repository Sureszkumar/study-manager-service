package com.study.manager.domain;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class CourseSettings {

	private WeeklyHours weeklyHours;

	private String proficiency;

	private LocalDate targetDate;

	public WeeklyHours getWeeklyHours() {
		return weeklyHours;
	}

	public void setWeeklyHours(WeeklyHours weeklyHours) {
		this.weeklyHours = weeklyHours;
	}

	public String getProficiency() {
		return proficiency;
	}

	public void setProficiency(String proficiency) {
		this.proficiency = proficiency;
	}

	public LocalDate getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(LocalDate targetDate) {
		this.targetDate = targetDate;
	}

}
