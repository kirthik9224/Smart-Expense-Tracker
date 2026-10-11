/*
 * Organization: Orbytes Global Solutions, Owner: Logaraj S, Created at: 2026-10-11
 * Description: Global exception handler for uncaught exceptions across controllers.
 */
package com.smartexpensetracker.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * Global exception handler for uncaught exceptions across controllers.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleNotFound(ResourceNotFoundException ex, Model model) {
        log.warn("Resource not found: {}", ex.getMessage());
        model.addAttribute("error", ex.getMessage());
        return "redirect:/dashboard";
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Void> handleNoResource(NoResourceFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model) {
        log.error("Unhandled application exception", ex);
        model.addAttribute("error", "An unexpected error occurred: " + ex.getMessage());
        return "login";
    }
}
