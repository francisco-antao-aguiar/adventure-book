package com.example.adventure_book.controller;

import com.example.adventure_book.dto.response.SectionResponse;
import com.example.adventure_book.service.SectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/book/{bookId}/sections")
@RequiredArgsConstructor
public class SectionController {
    private final SectionService sectionService;

    @GetMapping("start")
    public SectionResponse getStartSection(@PathVariable Long bookId) {
        return sectionService.getStartSection(bookId);
    }

    @GetMapping("{sectionNumber}")
    public SectionResponse getSection(@PathVariable Long bookId, @PathVariable Integer sectionNumber) {
        return sectionService.getSection(bookId, sectionNumber);
    }
}
