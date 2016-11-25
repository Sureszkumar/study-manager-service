package com.study.manager.service;


import static com.study.manager.util.ServiceUtils.copyNonNullProperties;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.study.manager.entity.BaseEntity;
import com.study.manager.repository.BaseRepository;
import com.study.manager.service.exception.ErrorCode;
import com.study.manager.service.exception.ServiceException;

public class BaseService<T extends BaseEntity> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseService.class);

    public BaseRepository<T> baseRepository;

    protected void init(BaseRepository<T> baseRepository) {
        this.baseRepository = baseRepository;
    }
    @Transactional
    public T create(T o) {
        LOGGER.debug("Creating {}", o);
        LocalDateTime now = LocalDateTime.now();
        o.setLastChangeTimestamp(now);
        o.setCreationDateTime(now);
        return baseRepository.save(o);
    }

    @Transactional
    public T update(T o) {
        LOGGER.debug("Updating {}", o);
        T existing = baseRepository.findOne(o.getId());
        if (existing == null) {
            throw new ServiceException(ErrorCode.SM_103);
        }
        copyNonNullProperties(o, existing);
        existing.setLastChangeTimestamp(LocalDateTime.now());
        o.setLastChangeTimestamp(LocalDateTime.now());
        return baseRepository.save(existing);
    }

    @Transactional
    public List<T> getAll() {
        LOGGER.debug("Retrieving all");
        return baseRepository.findAll();
    }

    @Transactional
    public void delete(Long id) {
        LOGGER.debug("Deleting entity with id : {}", id);
        T existing = baseRepository.findOne(id);
        if (existing == null) {
            throw new ServiceException(ErrorCode.SM_104);
        }
        baseRepository.delete(id);
    }

    @Transactional
    public T get(Long id) {
        LOGGER.debug("Retrieving entity with id : {}", id);
        T existing = baseRepository.findOne(id);
        if (existing == null) {
            throw new ServiceException(ErrorCode.SM_104);
        }
        return existing;
    }
}
