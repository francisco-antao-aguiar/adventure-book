package com.example.adventure_book.dto.request;

import com.example.adventure_book.type.ConsequenceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ConsequenceRequest(
        @NotNull ConsequenceType type,
        @NotBlank @Pattern(regexp = "\\d+") String value,
        String text
) {
}
