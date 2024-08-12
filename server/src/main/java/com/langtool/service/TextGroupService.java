package com.langtool.service;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.langtool.dto.TextGroupDto;
import com.langtool.model.TextGroupEntity;
import com.langtool.repository.TextGroupRepository;


@Service
public class TextGroupService {
    
    @Autowired
    TextGroupRepository textGroupRepository;

    public List<TextGroupDto> getAllTextGroups() {
        List<TextGroupEntity> all = textGroupRepository.findAll();
        System.out.println("found " +all.size() + " items");
        List<TextGroupDto> dtoList = convertTextGroupEntitiesToDtos(all);
        return dtoList;
    }

    private List<TextGroupDto> convertTextGroupEntitiesToDtos(List<TextGroupEntity> entities) {
        Map<Long, List<TextGroupEntity>> groupedByPhotoId = entities.stream()
            .collect(Collectors.groupingBy(TextGroupEntity::getPhotoId));

        return groupedByPhotoId.entrySet().stream()
            .map(entry -> {
                Long photoId = entry.getKey();
                List<TextGroupEntity> group = entry.getValue();
                String[] texts = group.stream()
                    .map(TextGroupEntity::getText)
                    .toArray(String[]::new);
                String srcFileName = "photo_" + photoId + ".jpg"; // Assuming a naming convention
                return new TextGroupDto(photoId, srcFileName, texts);
            })
            .collect(Collectors.toList());
    }
}
