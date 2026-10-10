package com.example.adventure_book.exception;

public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException(String player, Long bookId) {
        super("Player " + player + " has no game for book " + bookId);
    }
}
