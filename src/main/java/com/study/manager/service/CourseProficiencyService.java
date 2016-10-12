package com.study.manager.service;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.study.manager.entity.CourseProficiencyEntity;
import com.study.manager.repository.CourseProficiencyRepository;

@Service
@Validated
public class CourseProficiencyService {

    @Inject
    private CourseProficiencyRepository courseProficiencyRepository;
    
    public void add(CourseProficiencyEntity courseProficiencyEntity){
    	courseProficiencyRepository.save(courseProficiencyEntity);
    }
}
