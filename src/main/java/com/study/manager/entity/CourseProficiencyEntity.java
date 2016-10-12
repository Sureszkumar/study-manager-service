package com.study.manager.entity;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "COURSE_PROFICIENCY")
@NamedQueries({
	@NamedQuery(name = "CourseProficiencyEntity.findByCourseId", query = "SELECT c FROM CourseProficiencyEntity c WHERE c.courseId = ?1")
})
public class CourseProficiencyEntity extends BaseEntity {

	private long courseId;

	private int easyPages;

	private int normalPages;

	private int difficultPages;

	public long getCourseId() {
		return courseId;
	}

	public void setCourseId(long courseId) {
		this.courseId = courseId;
	}

	public int getEasyPages() {
		return easyPages;
	}

	public void setEasyPages(int easyPages) {
		this.easyPages = easyPages;
	}

	public int getNormalPages() {
		return normalPages;
	}

	public void setNormalPages(int normalPages) {
		this.normalPages = normalPages;
	}

	public int getDifficultPages() {
		return difficultPages;
	}

	public void setDifficultPages(int difficultPages) {
		this.difficultPages = difficultPages;
	}

}