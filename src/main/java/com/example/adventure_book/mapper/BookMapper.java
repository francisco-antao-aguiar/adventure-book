package com.example.adventure_book.mapper;

import com.example.adventure_book.dto.request.BookRequest;
import com.example.adventure_book.dto.response.BookResponse;
import com.example.adventure_book.entity.Book;
import com.example.adventure_book.entity.Category;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class BookMapper {

    private BookMapper() {
    }

    public static BookResponse toResponse(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getDifficulty(),
                book.getCategories().stream()
                        .map(Category::getName)
                        .collect(Collectors.toCollection(TreeSet::new))
        );
    }

    public static Book toEntity(BookRequest request, Set<Category> categories) {
        Book book = new Book();
        book.setTitle(request.title().trim());
        book.setAuthor(request.author().trim());
        book.setDifficulty(request.difficulty());
        categories.forEach(book::addCategory);
        request.sections().forEach(sr -> book.addSection(SectionMapper.toEntity(sr)));
        return book;
    }
}
