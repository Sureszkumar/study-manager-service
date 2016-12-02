package com.study.manager.translator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.study.manager.domain.Book;
import com.study.manager.domain.Type;
import com.study.manager.entity.UserCourseBooksEntity;

@Component
public class UserCourseBooksTranslator {

    public List<UserCourseBooksEntity> translateToEntity(List<Book> books) {
        List<UserCourseBooksEntity> bookEntityList = new ArrayList<>();
        for (Book book : books) {
            bookEntityList.add(translateToEntity(book));
        }
        return bookEntityList;
    }

    public UserCourseBooksEntity translateToEntity(Book book) {
        UserCourseBooksEntity bookEntity = new UserCourseBooksEntity();
        bookEntity.setTitle(book.getTitle());
        bookEntity.setDescription(book.getDescription());
        bookEntity.setType(Type.valueOf(book.getType()));
        bookEntity.setAuthor(book.getAuthor());
        bookEntity.setTotalNoOfPages(book.getNoOfPages());
        bookEntity.setNoOfPagesUnRead(book.getNoOfPages());
        bookEntity.setRevisionCompleted(book.isRevisionCompleted());
        return bookEntity;
    }
}
