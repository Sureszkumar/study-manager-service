package com.study.manager.translator;

import com.study.manager.domain.ProficiencyValue;
import com.study.manager.entity.CourseProficiencyEntity;
import org.springframework.stereotype.Component;

import com.study.manager.domain.CourseSettings;
import com.study.manager.domain.WeeklyHours;
import com.study.manager.entity.UserCoursesEntity;
import com.study.manager.entity.WeekEntity;

@Component
public class CourseSettingsTranslator {

	public CourseSettings translateToDomain(UserCoursesEntity userCoursesEntity, CourseProficiencyEntity courseProficiencyEntity) {
		CourseSettings courseSettings = new CourseSettings();
		courseSettings.setProficiency(userCoursesEntity.getProficiency());
		courseSettings.setTargetDate(userCoursesEntity.getEndDate());
		WeeklyHours weeklyHours = new WeeklyHours();
		WeekEntity weekEntity = userCoursesEntity.getWeeklyHoursEntity().getWeekEntity();
		weeklyHours.setMonday(weekEntity.getMonday());
		weeklyHours.setTuesday(weekEntity.getTuesday());
		weeklyHours.setWednesday(weekEntity.getWednesday());
		weeklyHours.setThursday(weekEntity.getThursday());
		weeklyHours.setFriday(weekEntity.getFriday());
		weeklyHours.setSaturday(weekEntity.getSaturday());
		weeklyHours.setSunday(weekEntity.getSunday());
		courseSettings.setWeeklyHours(weeklyHours);
		courseSettings.setDefaultView(userCoursesEntity.getDefaultSettingsView());
		ProficiencyValue proficiencyValue = new ProficiencyValue();
		proficiencyValue.setEasy(courseProficiencyEntity.getEasyPages());
		proficiencyValue.setModerate(courseProficiencyEntity.getModeratePages());
		proficiencyValue.setDifficult(courseProficiencyEntity.getDifficultPages());
		courseSettings.setProficiencyValue(proficiencyValue);
		return courseSettings;
	}

}
