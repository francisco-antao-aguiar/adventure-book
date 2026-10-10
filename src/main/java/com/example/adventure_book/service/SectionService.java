package com.example.adventure_book.service;

import com.example.adventure_book.dto.response.SectionResponse;
import com.example.adventure_book.entity.Section;
import com.example.adventure_book.exception.SectionNotFoundException;
import com.example.adventure_book.mapper.SectionMapper;
import com.example.adventure_book.repository.SectionRepository;
import com.example.adventure_book.type.SectionType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SectionService {
    private final SectionRepository sectionRepository;

    private final BookService bookService;

    @Transactional
    public SectionResponse getStartSection(Long bookId) {
        return SectionMapper.toResponse(findStartSection(bookId));
    }

    public Section findStartSection(Long bookId) {
        bookService.checkBookExists(bookId);
        return sectionRepository.findByBookIdAndType(bookId, SectionType.BEGIN)
                .orElseThrow(() -> new SectionNotFoundException(bookId, null));
    }

    @Transactional
    public SectionResponse getSection(Long bookId, Integer sectionNumber) {
        return SectionMapper.toResponse(findSection(bookId, sectionNumber));
    }

    public Section findSection(Long bookId, Integer sectionNumber) {
        bookService.checkBookExists(bookId);
        return sectionRepository.findByBookIdAndSectionNumber(bookId, sectionNumber)
                .orElseThrow(() -> new SectionNotFoundException(bookId, sectionNumber));
    }
}
