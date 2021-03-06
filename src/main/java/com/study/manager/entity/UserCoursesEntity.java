package com.study.manager.entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "USER_COURSES")
@NamedQueries({
        @NamedQuery(name = "UserCoursesEntity.findAllCourses", query = "SELECT u.courseId FROM UserCoursesEntity u WHERE u.userId = ?1"),
        @NamedQuery(name = "UserCoursesEntity.findAllByUserId", query = "SELECT u from UserCoursesEntity u WHERE u.userId = ?1"),
        @NamedQuery(name = "UserCoursesEntity.findCount", query = "SELECT count(u) FROM UserCoursesEntity u WHERE u.userId = ?1 and u.courseId = ?2"),
        @NamedQuery(name = "UserCoursesEntity.findBy", query = "SELECT u FROM UserCoursesEntity u WHERE u.userId = ?1 and u.courseId = ?2"),})
public class UserCoursesEntity extends BaseEntity {

    private Long userId;

    private Long courseId;

    private LocalDate startDate;

    private LocalDate endDate;

    private String proficiency;

    private String currentStatus;

    private double completionRate;

    private int totalNoOfPages;

    private int pagesRead;

    private int pagesUnRead;

    private int todayGoal;

    private String defaultSettingsView;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_course_id")
    private List<UserCourseBooksEntity> userCourseBooksEntity;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "weekly_hours_id")
    private WeeklyHoursEntity weeklyHoursEntity;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "weekly_pages_id")
    private WeeklyPagesEntity weeklyPagesEntity;

    public List<UserCourseBooksEntity> getUserCourseBooksEntity() {
        return userCourseBooksEntity;
    }

    public void setUserCourseBooksEntity(List<UserCourseBooksEntity> userCourseBooksEntity) {
        this.userCourseBooksEntity = userCourseBooksEntity;
    }

    public int getTodayGoal() {
        return todayGoal;
    }

    public void setTodayGoal(int todayGoal) {
        this.todayGoal = todayGoal;
    }

    public int getPagesUnRead() {
        return pagesUnRead;
    }

    public void setPagesUnRead(int pagesUnRead) {
        this.pagesUnRead = pagesUnRead;
    }

    public int getTotalNoOfPages() {
        return totalNoOfPages;
    }

    public void setTotalNoOfPages(int totalNoOfPages) {
        this.totalNoOfPages = totalNoOfPages;
    }

    public int getPagesRead() {
        return pagesRead;
    }

    public void setPagesRead(int pagesRead) {
        this.pagesRead = pagesRead;
    }

    public String getProficiency() {
        return proficiency;
    }

    public void setProficiency(String proficiency) {
        this.proficiency = proficiency;
    }

    public WeeklyPagesEntity getWeeklyPagesEntity() {
        return weeklyPagesEntity;
    }

    public void setWeeklyPagesEntity(WeeklyPagesEntity weeklyPagesEntity) {
        this.weeklyPagesEntity = weeklyPagesEntity;
    }

    public WeeklyHoursEntity getWeeklyHoursEntity() {
        return weeklyHoursEntity;
    }

    public void setWeeklyHoursEntity(WeeklyHoursEntity weeklyHoursEntity) {
        this.weeklyHoursEntity = weeklyHoursEntity;
    }

    public UserCoursesEntity() {

    }

    public UserCoursesEntity(Long userId, Long courseId) {
        this.userId = userId;
        this.courseId = courseId;
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

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public Long getUserId() {

        return userId;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public double getCompletionRate() {
        return completionRate;
    }

    public void setCompletionRate(double completionRate) {
        this.completionRate = completionRate;
    }

    public String getDefaultSettingsView() {
        return defaultSettingsView;
    }

    public void setDefaultSettingsView(String defaultSettingsView) {
        this.defaultSettingsView = defaultSettingsView;
    }
}
