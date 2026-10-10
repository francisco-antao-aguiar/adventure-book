package com.example.adventure_book.dto.request;

import jakarta.validation.constraints.NotNull;

public record MoveRequest(
        @NotNull Long optionId
) {
}
