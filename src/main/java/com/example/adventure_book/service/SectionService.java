package com.example.adventure_book.service;

import com.example.adventure_book.dto.response.SectionResponse;
import com.example.adventure_book.entity.Section;
import com.example.adventure_book.exception.BookNotFoundException;
import com.example.adventure_book.exception.SectionNotFoundException;
import com.example.adventure_book.mapper.SectionMapper;
import com.example.adventure_book.repository.BookRepository;
import com.example.adventure_book.repository.SectionRepository;
import com.example.adventure_book.type.SectionType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SectionService {
    private final BookRepository bookRepository;
    private final SectionRepository sectionRepository;

    @Transactional
    public SectionResponse getStartSection(Long bookId) {
        checkBookExists(bookId);
        Section section = sectionRepository.findByBookIdAndType(bookId, SectionType.BEGIN)
                .orElseThrow(() -> new SectionNotFoundException(bookId, null));
        return SectionMapper.toResponse(section);
    }

    @Transactional
    public SectionResponse getSection(Long bookId, Integer sectionNumber) {
        checkBookExists(bookId);
        Section section = sectionRepository.findByBookIdAndSectionNumber(bookId, sectionNumber)
                .orElseThrow(() -> new SectionNotFoundException(bookId, sectionNumber));
        return SectionMapper.toResponse(section);
    }

    private void checkBookExists(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new BookNotFoundException(bookId);
        }
    }
}
