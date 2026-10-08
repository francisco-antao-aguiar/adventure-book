package com.example.adventure_book.dto.response;

import com.example.adventure_book.type.Difficulty;

import java.util.Set;

public record BookResponse(
        Long id,
        String title,
        String author,
        Difficulty difficulty,
        Set<String> categories
) {
}
