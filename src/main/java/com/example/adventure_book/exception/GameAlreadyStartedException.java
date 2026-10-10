package com.example.adventure_book.exception;

public class GameAlreadyStartedException extends RuntimeException {
    public GameAlreadyStartedException(String player, Long bookId) {
        super("Player " + player + " already has a game for book " + bookId);
    }
}
