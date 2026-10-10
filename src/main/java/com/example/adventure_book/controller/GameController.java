package com.example.adventure_book.controller;

import com.example.adventure_book.dto.request.PlayRequest;
import com.example.adventure_book.dto.response.PlayResponse;
import com.example.adventure_book.service.GameService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/book/{bookId}/play")
@RequiredArgsConstructor
public class GameController {
    private final GameService gameService;

    @PostMapping("start")
    public PlayResponse start(@PathVariable Long bookId) {
        return gameService.start(bookId);
    }

    @PostMapping
    public PlayResponse play(@PathVariable Long bookId, @Valid @RequestBody PlayRequest request) {
        return gameService.play(bookId, request);
    }
}
