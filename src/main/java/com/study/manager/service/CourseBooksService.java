package com.study.manager.service;

import com.study.manager.entity.BookEntity;
import com.study.manager.entity.UserCoursesEntity;
import com.study.manager.repository.BookRepository;
import com.study.manager.repository.CourseBooksRepository;
import com.study.manager.repository.UserCoursesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.inject.Inject;
import java.util.List;

@Service
@Validated
public class CourseBooksService {

    private Logger log = LoggerFactory.getLogger(CourseBooksService.class);

    @Inject
    private CourseBooksRepository courseBooksRepository;
    @Inject
    private BookRepository bookRepository;

    public List<BookEntity> findBooks(Long courseId) {
        List<Long> bookIds = courseBooksRepository.findBookIds(courseId);
        return bookRepository.findAll(bookIds);
    }
}
