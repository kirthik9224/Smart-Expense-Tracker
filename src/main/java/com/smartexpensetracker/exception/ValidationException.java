package com.smartexpensetracker.exception;

/**
 * Exception thrown when business logic validation fails.
 */
public class ValidationException extends AppException {
    public ValidationException(String message) {
        super(message);
    }
}
