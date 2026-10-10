package com.example.adventure_book.exception;

public class GameFinishedException extends RuntimeException {
    public GameFinishedException(String player, Long bookId) {
        super("Game of player " + player + " for book " + bookId + " is already finished");
    }
}
