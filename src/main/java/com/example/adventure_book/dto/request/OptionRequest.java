package com.example.adventure_book.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record OptionRequest(
        @NotBlank @Size(max = 255) String description,
        @NotNull Integer gotoId,
        @Valid ConsequenceRequest consequence
) {
}
