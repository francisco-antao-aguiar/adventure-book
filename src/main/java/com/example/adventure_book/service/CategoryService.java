package com.example.adventure_book.service;

import com.example.adventure_book.entity.Category;
import com.example.adventure_book.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    public List<String> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(Category::getName)
                .toList();
    }

    public Category findOrCreateCategory(String name) {
        String normalized = Category.normalize(name);
        return categoryRepository.findByName(normalized)
                .orElseGet(() -> categoryRepository.save(new Category(normalized)));
    }

    public Set<Category> findOrCreateCategories(Set<String> names) {
        if (names == null) {
            return Set.of();
        }
        return names.stream()
                .map(Category::normalize)
                .distinct()
                .map(this::findOrCreateCategory)
                .collect(Collectors.toSet());
    }
}
