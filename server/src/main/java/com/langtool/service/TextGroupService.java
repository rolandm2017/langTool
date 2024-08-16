package com.langtool.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;

import com.langtool.dao.TextGroupDao;
import com.langtool.dto.EntityToDtoConverter;
import com.langtool.dto.TextGroupDto;
import com.langtool.model.TextGroupEntity;
import com.langtool.model.WordEntity;
import com.langtool.repository.TextGroupRepository;


@Service
public class TextGroupService {
    
    @Autowired
    TextGroupDao textGroupDao;

    public List<TextGroupDto> getAllTextGroups() {
        List<TextGroupEntity> all = textGroupDao.findAllTextGroups();
        System.out.println("found " +all.size() + " items");
        List<TextGroupDto> dtoList = EntityToDtoConverter.convertTextGroupEntitiesToDtos(all);
        return dtoList;
    }



    
}
