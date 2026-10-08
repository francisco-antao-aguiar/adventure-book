package com.example.adventure_book.mapper;

import com.example.adventure_book.dto.request.ConsequenceRequest;
import com.example.adventure_book.dto.request.OptionRequest;
import com.example.adventure_book.dto.request.SectionRequest;
import com.example.adventure_book.dto.response.ConsequenceResponse;
import com.example.adventure_book.dto.response.OptionResponse;
import com.example.adventure_book.dto.response.SectionResponse;
import com.example.adventure_book.entity.Consequence;
import com.example.adventure_book.entity.Section;
import com.example.adventure_book.entity.SectionOption;

public final class SectionMapper {

    private SectionMapper() {
    }

    public static SectionResponse toResponse(Section section) {
        return new SectionResponse(
                section.getSectionNumber(),
                section.getText(),
                section.getType(),
                section.getOptions().stream()
                        .map(SectionMapper::toResponse)
                        .toList());
    }

    public static OptionResponse toResponse(SectionOption option) {
        return new OptionResponse(
                option.getId(),
                option.getDescription(),
                option.getTarget().getSectionNumber(),
                toResponse(option.getConsequence()));
    }

    public static ConsequenceResponse toResponse(Consequence consequence) {
        if (consequence == null) {
            return null;
        }
        return new ConsequenceResponse(
                consequence.getType(),
                consequence.getAmount(),
                consequence.getText());
    }

    public static Section toEntity(SectionRequest request) {
        Section section = new Section();
        section.setSectionNumber(request.id());
        section.setText(request.text());
        section.setType(request.type());
        return section;
    }

    public static SectionOption toEntity(OptionRequest request, Section target) {
        SectionOption option = new SectionOption();
        option.setDescription(request.description());
        option.setTarget(target);
        option.setConsequence(toEntity(request.consequence()));
        return option;
    }

    public static Consequence toEntity(ConsequenceRequest request) {
        if (request == null) {
            return null;
        }
        Consequence consequence = new Consequence();
        consequence.setType(request.type());
        consequence.setAmount(Integer.parseInt(request.value().trim()));
        consequence.setText(request.text());
        return consequence;
    }
}
