package com.example.adventure_book;

import com.example.adventure_book.dto.request.BookRequest;
import com.example.adventure_book.dto.request.OptionRequest;
import com.example.adventure_book.dto.request.SectionRequest;
import com.example.adventure_book.dto.response.BookResponse;
import com.example.adventure_book.entity.Book;
import com.example.adventure_book.entity.Category;
import com.example.adventure_book.entity.Section;
import com.example.adventure_book.exception.BookNotFoundException;
import com.example.adventure_book.repository.BookRepository;
import com.example.adventure_book.service.BookService;
import com.example.adventure_book.service.CategoryService;
import com.example.adventure_book.type.Difficulty;
import com.example.adventure_book.type.SectionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private BookService bookService;

    private static Book book(Long id, String title) {
        Book book = new Book();
        book.setId(id);
        book.setTitle(title);
        book.setAuthor("Me");
        book.setDifficulty(Difficulty.EASY);
        return book;
    }

    @Test
    void getBookById_returnsBook() {
        Book book = book(1L, "The Cave");
        book.addCategory(new Category("FANTASY"));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookResponse response = bookService.getBookById(1L);

        assertThat(response.title()).isEqualTo("The Cave");
        assertThat(response.categories()).containsExactly("FANTASY");
    }

    @Test
    void getBookById_throwsWhenBookDoesNotExist() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.getBookById(99L))
                .isInstanceOf(BookNotFoundException.class);
    }

    @Test
    void createBook_savesSectionsAndLinksOptions() {
        BookRequest bookRequest = new BookRequest(" The Cave ", "Me", Difficulty.EASY, Set.of("fantasy"), List.of(
                new SectionRequest(1, "Start", SectionType.BEGIN, List.of(new OptionRequest("Start", 2, null))),
                new SectionRequest(2, "The end", SectionType.END, null)));
        when(categoryService.findOrCreateCategories(Set.of("fantasy"))).thenReturn(Set.of(new Category("FANTASY")));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookResponse bookResponse = bookService.createBook(bookRequest);

        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(bookArgumentCaptor.capture());
        Book book = bookArgumentCaptor.getValue();
        assertThat(book.getSections()).hasSize(2);
        Section start = book.getSections().getFirst();
        assertThat(start.getOptions()).hasSize(1);
        assertThat(start.getOptions().getFirst().getTarget().getSectionNumber()).isEqualTo(2);
        assertThat(bookResponse.title()).isEqualTo("The Cave");
        assertThat(bookResponse.categories()).containsExactly("FANTASY");
    }

    @Test
    void addCategory_addsCategoryToBook() {
        Book book = book(1L, "The Cave");
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(categoryService.findOrCreateCategory("horror")).thenReturn(new Category("HORROR"));

        BookResponse response = bookService.addCategory(1L, "horror");

        assertThat(response.categories()).containsExactly("HORROR");
    }

    @Test
    void removeCategory_removesCategoryIgnoringCase() {
        Book book = book(1L, "The Cave");
        book.addCategory(new Category("FANTASY"));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookResponse bookResponse = bookService.removeCategory(1L, " fantasy ");

        assertThat(bookResponse.categories()).isEmpty();
    }

    @Test
    void deleteBook_deletesBook() {
        Book book = book(1L, "The Cave");
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        bookService.deleteBook(1L);

        verify(bookRepository).delete(book);
    }

    @Test
    void deleteBook_throwsWhenBookDoesNotExist() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.deleteBook(99L))
                .isInstanceOf(BookNotFoundException.class);
        verify(bookRepository, never()).delete(any());
    }
}