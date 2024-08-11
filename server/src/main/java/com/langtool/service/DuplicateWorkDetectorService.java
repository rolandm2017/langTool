package com.langtool.service;

import org.springframework.stereotype.Service;

@Service
public class DuplicateWorkDetectorService {
    // Do not do this:
    // "write some sort of db table entry just to contain the first N bytes of a photo."
    // "for 3, the idea is that, if the photo has a different name and is uploaded twice ..."
    // That is YAGNI stuff.

    public boolean photoHasAlreadyBeenProcessed(String fileName) {
        // it's a read query on the Photo table. is the file name there or not?

        if (fileName == "already_seen!") { // todo
            return true;
        }
        return false;
    }

}

// 1. detect if the same photo is being submitted for photo->text
// 2. write some sort of db entry: "foo_file_1.jpg has already been processed."
