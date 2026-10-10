package com.example.adventure_book.dto.response;

public record PlayResponse(
        Integer health,
        boolean finished,
        SectionResponse section
) {
}
