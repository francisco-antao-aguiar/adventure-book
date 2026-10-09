package com.example.adventure_book.repository;

import com.example.adventure_book.entity.Section;
import com.example.adventure_book.type.SectionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SectionRepository extends JpaRepository<Section, Long> {
    Optional<Section> findByBookIdAndSectionNumber(Long bookId, Integer sectionNumber);

    Optional<Section> findByBookIdAndType(Long bookId, SectionType type);
}
