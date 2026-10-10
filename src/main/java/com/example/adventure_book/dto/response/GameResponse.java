package com.example.adventure_book.dto.response;

public record GameResponse(
        Long bookId,
        String bookTitle,
        Integer sectionNumber,
        Integer health,
        boolean finished
) {
}
