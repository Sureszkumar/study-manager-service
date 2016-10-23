package com.study.manager.controller;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.study.manager.service.UserCoursesService;

@Component
public class DailyGoalScheduler {

	@Inject
	UserCoursesService userCourseService;

	@Scheduled(cron = "59 59 23 * * *")
	public void doScheduledWork() {
		userCourseService.addDailyGoal();
	}
}
