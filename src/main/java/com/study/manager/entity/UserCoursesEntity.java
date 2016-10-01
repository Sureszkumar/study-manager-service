package com.study.manager.entity;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "USER_COURSES")
@NamedQueries({
	@NamedQuery(name = "UserCoursesEntity.findAllCourses", query = "SELECT u.courseId FROM UserCoursesEntity u WHERE u.userId = ?1"),
	@NamedQuery(name = "UserCoursesEntity.findCount", query = "SELECT count(u) FROM UserCoursesEntity u WHERE u.userId = ?1 and u.courseId = ?2"),
	@NamedQuery(name = "UserCoursesEntity.findBy", query = "SELECT u FROM UserCoursesEntity u WHERE u.userId = ?1 and u.courseId = ?2"),
})
public class UserCoursesEntity extends BaseEntity {

    private Long userId;

    private Long courseId;

    public UserCoursesEntity(){
    	
    }
    
    public UserCoursesEntity(Long userId, Long courseId) {
        this.userId = userId;
        this.courseId = courseId;
    }

    public Long getCourseId() {
        return courseId;
    }


    public Long getUserId() {

        return userId;
    }

}
