package com.study.manager.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.study.manager.domain.Type;

@Entity
@Table(name = "BOOK")
public class BookEntity extends BaseEntity {

	@NotNull
	private String title;

	private String description;
	
	@NotNull
	private long noOfPages;

	private String author;

	private String isbn;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private Type type;
	
	public long getNoOfPages() {
		return noOfPages;
	}

	public void setNoOfPages(long noOfPages) {
		this.noOfPages = noOfPages;
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

	public long getPages() {
		return noOfPages;
	}

	public void setPages(long pages) {
		this.noOfPages = pages;
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
