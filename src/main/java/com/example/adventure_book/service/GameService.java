package com.example.adventure_book.service;

import com.example.adventure_book.dto.request.PlayRequest;
import com.example.adventure_book.dto.response.PlayResponse;
import com.example.adventure_book.entity.Consequence;
import com.example.adventure_book.entity.Section;
import com.example.adventure_book.entity.SectionOption;
import com.example.adventure_book.exception.InvalidChoiceException;
import com.example.adventure_book.mapper.SectionMapper;
import com.example.adventure_book.type.SectionType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {
    private static final int START_HEALTH = 10;
    private final SectionService sectionService;

    private static int calculateConsequence(Consequence consequence) {
        return switch (consequence.getType()) {
            case LOSE_HEALTH -> consequence.getAmount() * -1;
            case GAIN_HEALTH -> consequence.getAmount();
        };
    }

    @Transactional
    public PlayResponse start(Long bookId) {
        Section start = sectionService.findStartSection(bookId);
        return new PlayResponse(START_HEALTH, false, SectionMapper.toResponse(start));
    }

    @Transactional
    public PlayResponse play(Long bookId, PlayRequest playRequest) {
        Section currentSection = sectionService.findSection(bookId, playRequest.sectionNumber());

        SectionOption sectionOption = currentSection.getOptions().stream()
                .filter(option -> option.getId().equals(playRequest.optionId()))
                .findFirst()
                .orElseThrow(() -> new InvalidChoiceException(playRequest.optionId(), playRequest.sectionNumber()));

        int health = playRequest.health();
        if (sectionOption.getConsequence() != null) {
            health += calculateConsequence(sectionOption.getConsequence());
        }

        if (health <= 0) {
            return new PlayResponse(0, true, SectionMapper.toResponse(currentSection));
        }

        Section target = sectionOption.getTarget();
        return new PlayResponse(health, target.getType() == SectionType.END, SectionMapper.toResponse(target));
    }
}
