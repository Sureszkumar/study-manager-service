package com.study.manager.entity;

import com.study.manager.domain.Type;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "USER_COURSE_BOOKS")
public class UserCourseBooksEntity extends BaseEntity {

    private String title;

    private String description;

    private int totalNoOfPages;

    private int noOfPagesRead;

    private int noOfPagesUnRead;

    private String author;

    private String isbn;
    
    private String imageUrl;
    
    private boolean revisionCompleted;
    
    public boolean isRevisionCompleted() {
		return revisionCompleted;
	}

	public void setRevisionCompleted(boolean revisionCompleted) {
		this.revisionCompleted = revisionCompleted;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}


    @NotNull
    @Enumerated(EnumType.STRING)
    private Type type;

    public int getTotalNoOfPages() {
        return totalNoOfPages;
    }

    public void setTotalNoOfPages(int totalNoOfPages) {
        this.totalNoOfPages = totalNoOfPages;
    }

    public int getNoOfPagesRead() {
        return noOfPagesRead;
    }

    public void setNoOfPagesRead(int noOfPagesRead) {
        this.noOfPagesRead = noOfPagesRead;
    }

    public int getNoOfPagesUnRead() {
        return noOfPagesUnRead;
    }

    public void setNoOfPagesUnRead(int noOfPagesUnRead) {
        this.noOfPagesUnRead = noOfPagesUnRead;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



}