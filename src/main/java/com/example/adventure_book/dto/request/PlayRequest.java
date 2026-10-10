package com.example.adventure_book.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PlayRequest(
        @NotNull Integer sectionNumber,
        @NotNull @Min(1) Integer health,
        @NotNull Long optionId
) {
}
