package com.example.adventure_book.repository;

import com.example.adventure_book.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {
    Optional<Game> findByPlayerAndBookId(String player, Long bookId);

    boolean existsByPlayerAndBookId(String player, Long bookId);

    List<Game> findByPlayer(String player);
}
