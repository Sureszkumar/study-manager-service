package com.study.manager.entity;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "COURSE_BOOKS")

@NamedQueries({
	@NamedQuery(name = "CourseBooksEntity.findBookIds", query = "SELECT u.bookId FROM CourseBooksEntity u WHERE u.courseId = ?1")
})
public class CourseBooksEntity extends BaseEntity {

	private Long courseId;
    
	private Long bookId;

    public CourseBooksEntity(Long courseId, Long bookId) {
        this.courseId = courseId;
        this.bookId = bookId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public Long getBookId() {

        return bookId;
    }

}