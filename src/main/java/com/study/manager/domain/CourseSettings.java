package com.study.manager.domain;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class CourseSettings {

	private WeeklyHours weeklyHours;

	private String proficiency;

	private LocalDate targetDate;
	
	private LocalDate nearestTargetDate;

	private String defaultView;

	private ProficiencyValue proficiencyValue;
	

	public LocalDate getNearestTargetDate() {
		return nearestTargetDate;
	}

	public void setNearestTargetDate(LocalDate nearestTargetDate) {
		this.nearestTargetDate = nearestTargetDate;
	}

	public ProficiencyValue getProficiencyValue() {
		return proficiencyValue;
	}

	public void setProficiencyValue(ProficiencyValue proficiencyValue) {
		this.proficiencyValue = proficiencyValue;
	}

	public String getDefaultView() {
		return defaultView;
	}

	public void setDefaultView(String defaultView) {
		this.defaultView = defaultView;
	}

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
