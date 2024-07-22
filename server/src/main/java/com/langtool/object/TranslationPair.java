package com.langtool.object;

public class TranslationPair {
    private String sourceEnglishWord;
    private String translatedFrench;

    public TranslationPair(String sourceEnglishWord, String translatedFrench) {
        this.sourceEnglishWord = sourceEnglishWord;
        this.translatedFrench = translatedFrench;
    }

    public String getSourceEnglishWord() {
        return sourceEnglishWord;
    }

    public void setSourceEnglishWord(String sourceEnglishWord) {
        this.sourceEnglishWord = sourceEnglishWord;
    }

    public String getTranslatedFrench() {
        return translatedFrench;
    }

    public void setTranslatedFrench(String translatedFrench) {
        this.translatedFrench = translatedFrench;
    }
}
