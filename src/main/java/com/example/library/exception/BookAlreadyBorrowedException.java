package com.example.library.exception;

/**
 * Thrown when trying to borrow a book that is already borrowed
 * (i.e. an existing BorrowRecord with returnDate == null).
 * Mapped to HTTP 409 Conflict.
 */
public class BookAlreadyBorrowedException extends RuntimeException {

    public BookAlreadyBorrowedException(Long bookId) {
        super("Book with id " + bookId + " is currently borrowed and cannot be borrowed again");
    }
}
