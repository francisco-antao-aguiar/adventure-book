package com.example.adventure_book;

import com.example.adventure_book.entity.Category;
import com.example.adventure_book.repository.CategoryRepository;
import com.example.adventure_book.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void getAllCategories_returnsNames() {
        when(categoryRepository.findAll()).thenReturn(List.of(new Category("FANTASY"), new Category("HORROR")));

        assertThat(categoryService.getAllCategories()).containsExactly("FANTASY", "HORROR");
    }

    @Test
    void findOrCreateCategory_returnsExistingCategory() {
        Category horror = new Category("HORROR");
        when(categoryRepository.findByName("HORROR")).thenReturn(Optional.of(horror));

        Category result = categoryService.findOrCreateCategory(" horror ");

        assertThat(result).isSameAs(horror);
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void findOrCreateCategory_createsNormalizedCategoryWhenMissing() {
        when(categoryRepository.findByName("HORROR")).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Category result = categoryService.findOrCreateCategory(" horror ");

        assertThat(result.getName()).isEqualTo("HORROR");
    }
}
