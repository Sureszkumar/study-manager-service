package com.study.manager.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Book {

	private Long id;

	private String title;

	private String description;

	private String type;

	private String author;
	
	private int noOfPages;

	private int noOfPagesRead;

	private int noOfPagesUnRead;

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

	public int getNoOfPages() {
		return noOfPages;
	}

	public void setNoOfPages(int noOfPages) {
		this.noOfPages = noOfPages;
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
