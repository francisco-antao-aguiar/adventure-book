package com.example.adventure_book.controller;

import com.example.adventure_book.dto.request.BookRequest;
import com.example.adventure_book.dto.request.CategoryRequest;
import com.example.adventure_book.dto.response.BookResponse;
import com.example.adventure_book.service.BookService;
import com.example.adventure_book.type.Difficulty;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping
    public List<BookResponse> getBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("{id}")
    public BookResponse getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @GetMapping("search")
    public List<BookResponse> getBooks(@RequestParam(required = false) String title,
                                       @RequestParam(required = false) String author,
                                       @RequestParam(required = false) String category,
                                       @RequestParam(required = false) Difficulty difficulty) {
        return bookService.getBooks(title, author, category, difficulty);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponse createBook(@Valid @RequestBody BookRequest bookRequest) {
        return bookService.createBook(bookRequest);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }

    @PostMapping("{id}/categories")
    public BookResponse addCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
        return bookService.addCategory(id, request.name());
    }

    @DeleteMapping("{id}/categories/{name}")
    public BookResponse removeCategory(@PathVariable Long id, @PathVariable String name) {
        return bookService.removeCategory(id, name);
    }
}
