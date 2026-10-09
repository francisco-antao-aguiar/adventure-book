package com.example.adventure_book.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SectionNotFoundException extends RuntimeException {

    public SectionNotFoundException(Long bookId, Integer sectionNumber) {
        super("Section " + sectionNumber + " not found in book " + bookId);
    }
}
