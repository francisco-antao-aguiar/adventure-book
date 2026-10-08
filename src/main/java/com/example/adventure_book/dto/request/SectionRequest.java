package com.example.adventure_book.dto.request;

import com.example.adventure_book.type.SectionType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record SectionRequest(
        @NotNull Integer id,
        @NotBlank @Size(max = 4000) String text,
        @NotNull SectionType type,
        List<@NotNull @Valid OptionRequest> options
) {
}
