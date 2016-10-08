package com.study.manager.service;

import com.study.manager.domain.Book;
import com.study.manager.entity.BookEntity;
import com.study.manager.entity.EntityType;
import com.study.manager.entity.ImageEntity;
import com.study.manager.repository.BookRepository;
import com.study.manager.repository.ImageRepository;
import com.study.manager.translator.BookTranslator;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.inject.Inject;
import java.time.LocalDateTime;

import static com.study.manager.entity.EntityType.BOOK;

@Service
@Validated
public class ImageService {

    @Inject
    private ImageRepository imageRepository;

    public void add(byte[] image, Long id, EntityType entityType) {
        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setImage(image);
        switch (entityType) {
            case BOOK:
                imageEntity.setBookId(id);
                imageRepository.save(imageEntity);
                break;
            case COURSE:
                imageEntity.setCourseId(id);
                imageRepository.save(imageEntity);
                break;
            case USER:
                imageEntity.setUserId(id);
                imageRepository.save(imageEntity);
                break;
        }
    }
}
