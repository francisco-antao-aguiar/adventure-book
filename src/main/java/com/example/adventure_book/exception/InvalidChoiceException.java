package com.example.adventure_book.exception;

public class InvalidChoiceException extends RuntimeException {
    public InvalidChoiceException(Long optionId, Integer sectionNumber) {
        super("Option " + optionId + " is not available in section " + sectionNumber);
    }
}
