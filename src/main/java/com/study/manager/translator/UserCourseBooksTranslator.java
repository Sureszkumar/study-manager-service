package com.study.manager.translator;

import com.study.manager.domain.Book;
import com.study.manager.domain.Type;
import com.study.manager.entity.BookEntity;
import com.study.manager.entity.UserCourseBooksEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
        return bookEntity;
    }
}
