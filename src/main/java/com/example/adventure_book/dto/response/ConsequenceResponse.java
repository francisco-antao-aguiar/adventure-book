package com.example.adventure_book.dto.response;

import com.example.adventure_book.type.ConsequenceType;

public record ConsequenceResponse(
        ConsequenceType type,
        Integer value,
        String text
) {
}
