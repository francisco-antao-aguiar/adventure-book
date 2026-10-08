package com.example.adventure_book.dto.response;

public record OptionResponse(
        Long id,
        String description,
        Integer gotoId,
        ConsequenceResponse consequence
) {
}
