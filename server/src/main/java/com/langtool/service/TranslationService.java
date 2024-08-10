package com.langtool.service;

import org.springframework.stereotype.Service;


import com.langtool.object.TranslationPair;

@Service
public class TranslationService {

    public TranslationPair[] translateWords(String[] words) {
        TranslationPair[] translationPairs = new TranslationPair[] {
            new TranslationPair("Lorem ipsum", "Loreme ipsume"),
            new TranslationPair("Dolor sit amet", "Dolore sitte amette"),
            new TranslationPair("Consectetur adipiscing", "Consecteteur adipiscinge"),
            new TranslationPair("Elit sed do", "Elitte seddo"),
            new TranslationPair("Eiusmod tempor", "Eiusmode temporre")
        };
        return translationPairs;
    }

    public String translateFrToEn(String fr) {
        // todo
        return "";
    }
}
