package com.study.manager.service;

import java.time.LocalDateTime;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.study.manager.domain.Book;
import com.study.manager.entity.BookEntity;
import com.study.manager.repository.BookRepository;
import com.study.manager.translator.BookTranslator;

@Service
@Validated
public class BookService {

	@Inject
	private BookTranslator bookTranslator;

	@Inject
	private BookRepository bookRepository;
	
	public void addCourse(Book book) {
		BookEntity bookEntity = bookTranslator.translateToEntity(book);
		bookEntity.setCreationDateTime(LocalDateTime.now());
		bookEntity.setLastChangeTimestamp(LocalDateTime.now());
		bookRepository.save(bookEntity);

	}

	public Book getCourse(Long bookeId) {
		BookEntity bookEntity = bookRepository.findOne(bookeId);
		return bookTranslator.translateToDomain(bookEntity);
	}
}
