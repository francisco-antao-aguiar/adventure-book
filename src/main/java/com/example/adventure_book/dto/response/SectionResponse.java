package com.example.adventure_book.dto.response;

import com.example.adventure_book.type.SectionType;

import java.util.List;

public record SectionResponse(
        Integer sectionNumber,
        String text,
        SectionType type,
        List<OptionResponse> options
) {
}
