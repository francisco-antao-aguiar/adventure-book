package com.example.adventure_book.service;

import com.example.adventure_book.entity.Category;
import com.example.adventure_book.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Set<Category> findOrCreateCategories(Set<String> names) {
        if (names == null) {
            return Set.of();
        }
        return names.stream()
                .map(category -> category.trim().toUpperCase())
                .distinct()
                .map(name -> categoryRepository.findByName(name)
                        .orElseGet(() -> categoryRepository.save(new Category(name))))
                .collect(Collectors.toSet());
    }
}
