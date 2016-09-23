package com.study.manager.entity;

import com.google.common.base.Objects;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "USER_COURSES")
public class UserCoursesEntity extends BaseEntity {

    private Long userId;

    private Long courseId;

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
