package com.langtool.service;

import java.io.InputStream;

import org.springframework.stereotype.Service;


@Service
public class DeckService {
    
    public int createDeck() {
        // turns a list of words (that is already in the db, even ticked/unticked for ignore/etc)
        // into a deck on the server,
        // stores the deck for DL,
        // and makes it available.
        // TODO
        return 0;
    }

    public void startDeckDownload() {
        // todo
    }

    public InputStream generateAnkiDeckStream(Long id, String deckName) {
        /*
         * Keep id, deckName for redundancy, & verification. They must match
         */
        // InputStream stream;   
        // opens a file and starts streaming the data. the file is an anki deck.
        // return new InputStream();
        return null; // temp
    }
}
