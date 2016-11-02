package com.study.manager.service;

import java.time.LocalDateTime;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import com.study.manager.domain.Book;
import com.study.manager.entity.BookEntity;
import com.study.manager.entity.BookImage;
import com.study.manager.repository.BookRepository;
import com.study.manager.translator.BookTranslator;

@Service
@Validated
public class BookService {

	@Inject
	private BookTranslator bookTranslator;

	@Inject
	private BookRepository bookRepository;
	
	@Inject
	private FileArchiveService fileArchiveService;
	
	public void addBook(Book book, MultipartFile image) {
		BookEntity bookEntity = bookTranslator.translateToEntity(book);
		BookImage bookImage = fileArchiveService.saveBookImageToS3(image);
		bookEntity.setBookImage(bookImage);
		bookEntity.setCreationDateTime(LocalDateTime.now());
		bookEntity.setLastChangeTimestamp(LocalDateTime.now());
		bookRepository.save(bookEntity);

	}

	public Book get(Long bookeId) {
		BookEntity bookEntity = bookRepository.findOne(bookeId);
		return bookTranslator.translateToDomain(bookEntity);
	}
}
