package com.study.manager.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.study.manager.domain.Book;
import com.study.manager.domain.Course;
import com.study.manager.entity.BookEntity;
import com.study.manager.entity.CourseBooksEntity;
import com.study.manager.entity.CourseEntity;
import com.study.manager.repository.BookRepository;
import com.study.manager.repository.CourseBooksRepository;
import com.study.manager.repository.CourseRepository;
import com.study.manager.translator.BookTranslator;
import com.study.manager.translator.CourseTranslator;
import com.study.manager.util.ServiceUtils;

@Service
@Validated
public class CourseService {

    @Inject
    private CourseRepository courseRepository;

    @Inject
    private CourseTranslator courseTranslator;

    @Inject
    private BookTranslator bookTranslator;

    @Inject
    private UserCoursesService userCoursesService;

    @Inject
    private CourseBooksService courseBooksService;

    @Inject
    private CourseBooksRepository courseBooksRepository;

    @Inject
    private BookRepository bookRepository;

    public List<Course> getAll(long userId) {
        List<CourseEntity> courseEntities = courseRepository.findAll();
        List<Course> courseList = courseTranslator.translateToDomain(courseEntities);
        for (Course course : courseList) {
            course.setSubscribed(userCoursesService.isSubscribed(course.getId(), userId));
        }
        return courseList;
    }


    public void addCourse(Course course) {
        CourseEntity courseEntity = courseTranslator.translateToEntity(course);
        List<Book> bookList = course.getBookList();
        int totalNOfPages = 0;
        for (Book book : bookList) {
            totalNOfPages += book.getNoOfPages();
        }
        List<BookEntity> savedbookList = null;
        if (bookList != null) {
            List<BookEntity> translateToEntity = bookTranslator.translateToEntity(bookList);
            savedbookList = bookRepository.save(translateToEntity);
        }

        courseEntity.setCreationDateTime(LocalDateTime.now());
        courseEntity.setLastChangeTimestamp(LocalDateTime.now());
        int defaultTimeInWeeks = ServiceUtils.getDefaultCoursePreparationTime(totalNOfPages, 18, 7);
        courseEntity.setDefaultTimeInWeeks(defaultTimeInWeeks);
        CourseEntity savedCourse = courseRepository.save(courseEntity);
        List<CourseBooksEntity> courseBooksEntities = new ArrayList<>();
        if (savedbookList != null) {
            for (BookEntity bookEntity : savedbookList) {
                CourseBooksEntity courseBooksEntity = new CourseBooksEntity(savedCourse.getId(), bookEntity.getId());
                courseBooksEntities.add(courseBooksEntity);
            }

            courseBooksRepository.save(courseBooksEntities);
        }

    }

    public Course getCourse(Long courseId) {
        CourseEntity courseEntity = courseRepository.findOne(courseId);
        List<Book> bookList = bookTranslator.translateToDomain(courseBooksService.findBooks(courseId));
        Course course = courseTranslator.translateToDomain(courseEntity);
        course.setBookList(bookList);
        return course;

    }

}
