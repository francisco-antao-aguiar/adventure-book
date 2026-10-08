package com.example.adventure_book.dto.request;

import com.example.adventure_book.type.Difficulty;
import com.example.adventure_book.type.SectionType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record BookRequest(
        @NotBlank @Size(max = 255) String title,
        @NotBlank @Size(max = 255) String author,
        @NotNull Difficulty difficulty,
        Set<@NotBlank @Size(max = 255) String> categories,
        @NotEmpty List<@NotNull @Valid SectionRequest> sections
) {

    @AssertTrue(message = "Book has none, or more than one beginning")
    public boolean isExactlyOneBeginning() {
        if (noSections()) {
            return true;
        }

        long countBegin = sections.stream()
                .filter(s -> s != null && s.type() == SectionType.BEGIN)
                .count();

        return countBegin == 1;
    }

    @AssertTrue(message = "Book has no ending")
    public boolean isHasEnding() {
        if (noSections()) {
            return true;
        }

        long countEnd = sections.stream()
                .filter(s -> s != null && s.type() == SectionType.END)
                .count();

        return countEnd >= 1;
    }

    @AssertTrue(message = "Section ids must be unique")
    public boolean isUniqueSectionIds() {
        if (noSections()) {
            return true;
        }

        List<Integer> ids = sectionIds().toList();

        return ids.size() == ids.stream().distinct().count();
    }

    @AssertTrue(message = "Book has invalid next section id")
    public boolean isAllGotoIdsExist() {
        if (noSections()) {
            return true;
        }

        Set<Integer> ids = sectionIds().collect(Collectors.toSet());

        return sections.stream()
                .filter(Objects::nonNull)
                .filter(s -> s.options() != null)
                .flatMap(s -> s.options().stream())
                .filter(o -> o != null && o.gotoId() != null)
                .allMatch(o -> ids.contains(o.gotoId()));
    }

    @AssertTrue(message = "A non-ending section has no options")
    public boolean isEveryNonEndingSectionHasOptions() {
        if (noSections()) {
            return true;
        }

        return sections.stream()
                .filter(Objects::nonNull)
                .filter(s -> s.type() != null && s.type() != SectionType.END)
                .allMatch(s -> s.options() != null && !s.options().isEmpty());
    }

    private boolean noSections() {
        return sections == null || sections.isEmpty();
    }

    private Stream<Integer> sectionIds() {
        return sections.stream()
                .filter(Objects::nonNull)
                .map(SectionRequest::id)
                .filter(Objects::nonNull);
    }
}