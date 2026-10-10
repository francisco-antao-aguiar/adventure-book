package com.example.adventure_book.exception;

public class SectionNotFoundException extends RuntimeException {
    public SectionNotFoundException(Long bookId, Integer sectionNumber) {
        super("Section " + sectionNumber + " not found in book " + bookId);
    }
}
