package com.example.adventure_book;

import com.example.adventure_book.dto.response.SectionResponse;
import com.example.adventure_book.entity.Section;
import com.example.adventure_book.exception.BookNotFoundException;
import com.example.adventure_book.exception.SectionNotFoundException;
import com.example.adventure_book.repository.SectionRepository;
import com.example.adventure_book.service.BookService;
import com.example.adventure_book.service.SectionService;
import com.example.adventure_book.type.SectionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SectionServiceTest {

    @Mock
    private SectionRepository sectionRepository;

    @Mock
    private BookService bookService;

    @InjectMocks
    private SectionService sectionService;

    private static Section section(int number, SectionType type) {
        Section section = new Section();
        section.setSectionNumber(number);
        section.setText("Section " + number);
        section.setType(type);
        return section;
    }

    @Test
    void getStartSection_returnsBeginSection() {
        when(sectionRepository.findByBookIdAndType(1L, SectionType.BEGIN))
                .thenReturn(Optional.of(section(1, SectionType.BEGIN)));

        SectionResponse sectionResponse = sectionService.getStartSection(1L);

        assertThat(sectionResponse.sectionNumber()).isEqualTo(1);
        assertThat(sectionResponse.type()).isEqualTo(SectionType.BEGIN);
    }

    @Test
    void getSection_returnsSectionByNumber() {
        when(sectionRepository.findByBookIdAndSectionNumber(1L, 100))
                .thenReturn(Optional.of(section(100, SectionType.NODE)));

        SectionResponse sectionResponse = sectionService.getSection(1L, 100);

        assertThat(sectionResponse.sectionNumber()).isEqualTo(100);
    }

    @Test
    void getSection_throwsWhenSectionDoesNotExist() {
        when(sectionRepository.findByBookIdAndSectionNumber(1L, 99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sectionService.getSection(1L, 99))
                .isInstanceOf(SectionNotFoundException.class);
    }

    @Test
    void getSection_throwsWhenBookDoesNotExist() {
        doThrow(new BookNotFoundException(99L)).when(bookService).checkBookExists(99L);

        assertThatThrownBy(() -> sectionService.getSection(99L, 1))
                .isInstanceOf(BookNotFoundException.class);
        verifyNoInteractions(sectionRepository);
    }
}
