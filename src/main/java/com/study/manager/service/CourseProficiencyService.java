package com.study.manager.service;

import javax.inject.Inject;

import com.study.manager.service.exception.ErrorCode;
import com.study.manager.service.exception.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.study.manager.entity.CourseProficiencyEntity;
import com.study.manager.repository.CourseProficiencyRepository;

@Service
@Validated
public class CourseProficiencyService {

    @Inject
    private CourseProficiencyRepository courseProficiencyRepository;

    public void add(CourseProficiencyEntity courseProficiencyEntity) {
        CourseProficiencyEntity courseProficiencyRepositoryByCourseId = courseProficiencyRepository.
                findByCourseId(courseProficiencyEntity.getCourseId());
        if (courseProficiencyRepositoryByCourseId != null) {
            throw new ServiceException(ErrorCode.SM_105);
        }
        courseProficiencyRepository.save(courseProficiencyEntity);
    }

    public CourseProficiencyEntity findByCourseId(long courseId){
        return courseProficiencyRepository.findByCourseId(courseId);
    }
}
