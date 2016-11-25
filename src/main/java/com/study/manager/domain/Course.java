package com.study.manager.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Course {

	private Long id;

	private String title;

	private String description;

	private String type;

	private Boolean subscribed;

	private List<Book> bookList;

	private Integer preparationTimeInWeeks;

	private Double preparationTimeInMonths;
	
	private LocalDate startDate;

	private LocalDate endDate;

	private String currentStatus;

	private BigDecimal completionRate;

	private Integer todayGoal;
	
	private LocalDate lastUpdatedDate;
	

	public Double getPreparationTimeInMonths() {
		return preparationTimeInMonths;
	}

	public void setPreparationTimeInMonths(Double preparationTimeInMonths) {
		this.preparationTimeInMonths = preparationTimeInMonths;
	}

	public LocalDate getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(LocalDate lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public Integer getTodayGoal() {
		return todayGoal;
	}

	public void setTodayGoal(Integer todayGoal) {
		this.todayGoal = todayGoal;
	}

	public Boolean getSubscribed() {
		return subscribed;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	public BigDecimal getCompletionRate() {
		return completionRate;
	}

	public void setCompletionRate(BigDecimal completionRate) {
		this.completionRate = completionRate;
	}

	public Integer getPreparationTimeInWeeks() {
		return preparationTimeInWeeks;
	}

	public void setPreparationTimeInWeeks(Integer preparationTimeInWeeks) {
		this.preparationTimeInWeeks = preparationTimeInWeeks;
	}

	public Boolean isSubscribed() {
		return subscribed;
	}

	public void setSubscribed(Boolean subscribed) {
		this.subscribed = subscribed;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Book> getBookList() {
		return bookList;
	}

	public void setBookList(List<Book> bookList) {
		this.bookList = bookList;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
