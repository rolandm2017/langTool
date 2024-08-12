package com.langtool.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.langtool.service.TextGroupService;
import com.langtool.dto.TextGroupDto;

@RestController
@RequestMapping("/api/textgroup")
public class TextGroupController {
    
    @Autowired
    TextGroupService textGroupService;

    @GetMapping("/all")
    public List<TextGroupDto> getAllTextGroups() {
        return textGroupService.getAllTextGroups();
    }
}
