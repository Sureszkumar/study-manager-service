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

	private int beginnerPages;

    private int normalPages;

    private int expertPages;
    
	public long getCourseId() {
		return courseId;
	}

	public void setCourseId(long courseId) {
		this.courseId = courseId;
	}

	public int getBeginnerPages() {
		return beginnerPages;
	}

	public void setBeginnerPages(int beginnerPages) {
		this.beginnerPages = beginnerPages;
	}

	public int getNormalPages() {
		return normalPages;
	}

	public void setNormalPages(int normalPages) {
		this.normalPages = normalPages;
	}

	public int getExpertPages() {
		return expertPages;
	}

	public void setExpertPages(int expertPages) {
		this.expertPages = expertPages;
	}

	

}