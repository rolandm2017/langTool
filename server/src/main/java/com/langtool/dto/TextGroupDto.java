package com.langtool.dto;

public class TextGroupDto {
    private Long id;
    private String srcFileName;
    private String[] texts;

    public TextGroupDto(Long id,  String srcFileName, String[] texts) {
        this.id = id;
        this.srcFileName = srcFileName;
        this.texts = texts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getSrcFileName() {
        return srcFileName;
    }

    public void setSrcFileName(String srcFileName) {
        this.srcFileName = srcFileName;
    }

    public String[] getTexts() {
        return texts;
    }

    public void setTexts(String[] texts) {
        this.texts = texts;
    }

}