package com.example.adventure_book.controller;

import com.example.adventure_book.dto.request.MoveRequest;
import com.example.adventure_book.dto.response.GameResponse;
import com.example.adventure_book.dto.response.PlayResponse;
import com.example.adventure_book.service.GameService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/players/{player}")
@RequiredArgsConstructor
public class PlayerGameController {
    private final GameService gameService;

    @GetMapping("games")
    public List<GameResponse> getGames(@PathVariable String player) {
        return gameService.getPlayerGames(player);
    }

    @PostMapping("book/{bookId}/play/start")
    @ResponseStatus(HttpStatus.CREATED)
    public PlayResponse start(@PathVariable String player, @PathVariable Long bookId) {
        return gameService.startPlayerGame(player, bookId);
    }

    @GetMapping("book/{bookId}/play")
    public PlayResponse getGame(@PathVariable String player, @PathVariable Long bookId) {
        return gameService.getPlayerGame(player, bookId);
    }

    @PostMapping("book/{bookId}/play")
    public PlayResponse play(@PathVariable String player, @PathVariable Long bookId,
                             @Valid @RequestBody MoveRequest request) {
        return gameService.playPlayerGame(player, bookId, request.optionId());
    }

    @DeleteMapping("book/{bookId}/play")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void stop(@PathVariable String player, @PathVariable Long bookId) {
        gameService.stopPlayerGame(player, bookId);
    }
}
