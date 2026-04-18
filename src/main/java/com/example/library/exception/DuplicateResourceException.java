package com.example.library.exception;

/**
 * Thrown when a unique constraint would be violated (e.g. duplicate ISBN / email).
 * Mapped to HTTP 409 by GlobalExceptionHandler.
 */
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }
}
