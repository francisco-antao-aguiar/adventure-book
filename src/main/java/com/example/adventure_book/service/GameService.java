package com.example.adventure_book.service;

import com.example.adventure_book.dto.request.PlayRequest;
import com.example.adventure_book.dto.response.GameResponse;
import com.example.adventure_book.dto.response.PlayResponse;
import com.example.adventure_book.entity.Book;
import com.example.adventure_book.entity.Consequence;
import com.example.adventure_book.entity.Game;
import com.example.adventure_book.entity.Section;
import com.example.adventure_book.entity.SectionOption;
import com.example.adventure_book.exception.GameAlreadyStartedException;
import com.example.adventure_book.exception.GameFinishedException;
import com.example.adventure_book.exception.GameNotFoundException;
import com.example.adventure_book.exception.InvalidChoiceException;
import com.example.adventure_book.mapper.SectionMapper;
import com.example.adventure_book.repository.GameRepository;
import com.example.adventure_book.type.SectionType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameService {
    private static final int START_HEALTH = 10;

    private final GameRepository gameRepository;

    private final SectionService sectionService;
    private final BookService bookService;

    private static int calculateConsequence(Consequence consequence) {
        return switch (consequence.getType()) {
            case LOSE_HEALTH -> consequence.getAmount() * -1;
            case GAIN_HEALTH -> consequence.getAmount();
        };
    }

    @Transactional
    public List<GameResponse> getPlayerGames(String player) {
        return gameRepository.findByPlayer(player).stream()
                .map(game ->
                        new GameResponse(
                                game.getBook().getId(),
                                game.getBook().getTitle(),
                                game.getSectionNumber(),
                                game.getHealth(),
                                game.isFinished()
                        )
                )
                .toList();
    }

    @Transactional
    public PlayResponse getPlayerGame(String player, Long bookId) {
        Game game = findGame(player, bookId);
        Section section = sectionService.findSection(bookId, game.getSectionNumber());
        return new PlayResponse(
                game.getHealth(),
                game.isFinished(),
                SectionMapper.toResponse(section)
        );
    }

    @Transactional
    public PlayResponse startPlayerGame(String player, Long bookId) {
        if (gameRepository.existsByPlayerAndBookId(player, bookId)) {
            throw new GameAlreadyStartedException(player, bookId);
        }

        Book book = bookService.findBookById(bookId);
        Section start = sectionService.findStartSection(bookId);

        Game game = gameRepository.findByPlayerAndBookId(player, bookId).orElseGet(Game::new);
        game.setPlayer(player);
        game.setBook(book);
        game.setSectionNumber(start.getSectionNumber());
        game.setHealth(START_HEALTH);
        game.setFinished(false);
        gameRepository.save(game);

        return new PlayResponse(
                START_HEALTH,
                false,
                SectionMapper.toResponse(start)
        );
    }

    @Transactional
    public PlayResponse start(Long bookId) {
        Section start = sectionService.findStartSection(bookId);
        return new PlayResponse(
                START_HEALTH,
                false,
                SectionMapper.toResponse(start));
    }

    @Transactional
    public PlayResponse playPlayerGame(String player, Long bookId, Long optionId) {
        Game game = findGame(player, bookId);
        if (game.isFinished()) {
            throw new GameFinishedException(player, bookId);
        }

        PlayResponse response = move(bookId, game.getSectionNumber(), game.getHealth(), optionId);

        game.setSectionNumber(response.section().sectionNumber());
        game.setHealth(response.health());
        game.setFinished(response.finished());
        return response;
    }

    @Transactional
    public PlayResponse play(Long bookId, PlayRequest playRequest) {
        return move(bookId, playRequest.sectionNumber(), playRequest.health(), playRequest.optionId());
    }

    private PlayResponse move(Long bookId, Integer sectionNumber, int health, Long optionId) {
        Section currentSection = sectionService.findSection(bookId, sectionNumber);

        SectionOption sectionOption = currentSection.getOptions().stream()
                .filter(option -> option.getId().equals(optionId))
                .findFirst()
                .orElseThrow(() -> new InvalidChoiceException(optionId, sectionNumber));

        if (sectionOption.getConsequence() != null) {
            health += calculateConsequence(sectionOption.getConsequence());
        }

        if (health <= 0) {
            return new PlayResponse(
                    0,
                    true,
                    SectionMapper.toResponse(currentSection)
            );
        }

        Section target = sectionOption.getTarget();
        return new PlayResponse(
                health,
                target.getType() == SectionType.END,
                SectionMapper.toResponse(target)
        );
    }

    @Transactional
    public void stopPlayerGame(String player, Long bookId) {
        gameRepository.delete(findGame(player, bookId));
    }

    private Game findGame(String player, Long bookId) {
        return gameRepository.findByPlayerAndBookId(player, bookId)
                .orElseThrow(() -> new GameNotFoundException(player, bookId));
    }
}
