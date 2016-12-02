package com.study.manager.translator;

import com.study.manager.domain.Book;
import com.study.manager.domain.Type;
import com.study.manager.entity.BookEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class BookTranslator {


    public List<Book> translateToDomain(List<BookEntity> bookEntities) {

        List<Book> bookList = new ArrayList<>();
        for (BookEntity bookEntity : bookEntities) {
            Book book = new Book();
            book.setId(bookEntity.getId());
            book.setTitle(bookEntity.getTitle());
            book.setDescription(bookEntity.getDescription());
            book.setType(bookEntity.getType().name());
            book.setAuthor(bookEntity.getAuthor());
            book.setNoOfPages(bookEntity.getNoOfPages());
            book.setImageUrl(bookEntity.getImageUrl());
            bookList.add(book);
        }
        return bookList;
    }
    public Book translateToDomain(BookEntity bookEntity) {

        Book book = new Book();
        book.setId(bookEntity.getId());
        book.setTitle(bookEntity.getTitle());
        book.setDescription(bookEntity.getDescription());
        book.setType(bookEntity.getType().name());
        return book;
    }

    public BookEntity translateToEntity(Book book) {
        BookEntity bookEntity = new BookEntity();
        bookEntity.setTitle(book.getTitle());
        bookEntity.setDescription(book.getDescription());
        bookEntity.setType(Type.valueOf(book.getType()));
        return bookEntity;
    }

    public List<BookEntity> translateToEntity(List<Book> books) {
        List<BookEntity> bookEntityList = new ArrayList<>();
        for (Book book: books) {
            BookEntity bookEntity = new BookEntity();
            bookEntity.setTitle(book.getTitle());
            bookEntity.setDescription(book.getDescription());
            bookEntity.setType(Type.valueOf(book.getType()));
            bookEntity.setAuthor(book.getAuthor());
            bookEntity.setNoOfPages(book.getNoOfPages());
            bookEntity.setImageUrl(book.getImageUrl());
            bookEntity.setCreationDateTime(LocalDateTime.now());
            bookEntity.setLastChangeTimestamp(LocalDateTime.now());
            bookEntityList.add(bookEntity);
        }
        return bookEntityList;
    }
}
