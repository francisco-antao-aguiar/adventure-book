package com.example.adventure_book.service;

import com.example.adventure_book.dto.request.BookRequest;
import com.example.adventure_book.dto.request.OptionRequest;
import com.example.adventure_book.dto.request.SectionRequest;
import com.example.adventure_book.dto.response.BookResponse;
import com.example.adventure_book.entity.Book;
import com.example.adventure_book.entity.Category;
import com.example.adventure_book.entity.Section;
import com.example.adventure_book.exception.BookNotFoundException;
import com.example.adventure_book.mapper.BookMapper;
import com.example.adventure_book.mapper.SectionMapper;
import com.example.adventure_book.repository.BookRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    private final CategoryService categoryService;

    private static void addOptions(Book book, BookRequest request) {
        Map<Integer, Section> sectionsByNumber = book.getSections().stream()
                .collect(Collectors.toMap(Section::getSectionNumber, Function.identity()));

        for (SectionRequest sr : request.sections()) {
            if (sr.options() == null) continue;
            Section section = sectionsByNumber.get(sr.id());
            for (OptionRequest or : sr.options()) {
                section.addOption(SectionMapper.toEntity(or, sectionsByNumber.get(or.gotoId())));
            }
        }
    }

    @Transactional
    public List<BookResponse> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return books.stream().map(BookMapper::toResponse).toList();
    }

    @Transactional
    public BookResponse createBook(BookRequest bookRequest) {
        Set<Category> categories = categoryService.findOrCreateCategories(bookRequest.categories());

        Book book = BookMapper.toEntity(bookRequest, categories);

        addOptions(book, bookRequest);

        return BookMapper.toResponse(bookRepository.save(book));
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        book.getSections().forEach(section -> section.getOptions().clear());
        bookRepository.flush();
        bookRepository.delete(book);
    }

}
