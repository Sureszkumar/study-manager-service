package com.study.manager.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.study.manager.entity.BookEntity;
import com.study.manager.entity.CourseBooksEntity;
import com.study.manager.repository.BookRepository;
import com.study.manager.repository.CourseBooksRepository;

@Service
@Validated
public class CourseBooksService {

    @Inject
    private CourseBooksRepository courseBooksRepository;
    @Inject
    private BookRepository bookRepository;

    public List<BookEntity> findBooks(Long courseId) {
        List<Long> bookIds = courseBooksRepository.findBookIds(courseId);
        return bookRepository.findAll(bookIds);
    }

	public void linkCourseBook(Long courseId, Long bookId) {
		courseBooksRepository.save(new CourseBooksEntity(courseId, bookId));
		
	}
}
