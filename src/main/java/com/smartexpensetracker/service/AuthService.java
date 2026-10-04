package com.smartexpensetracker.service;

import com.smartexpensetracker.dto.LoginRequest;
import com.smartexpensetracker.dto.RegisterRequest;
import com.smartexpensetracker.exception.ValidationException;
import com.smartexpensetracker.model.User;
import com.smartexpensetracker.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service managing user authentication, registration, and credential verification.
 */
@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(RegisterRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new ValidationException("Email address is required.");
        }
        if (request.getPassword() == null || request.getPassword().length() < 6) {
            throw new ValidationException("Password must be at least 6 characters.");
        }
        if (userRepository.existsByEmail(request.getEmail().trim().toLowerCase())) {
            throw new ValidationException("An account with this email already exists.");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        userRepository.save(request.getName().trim(), request.getEmail().trim().toLowerCase(), encodedPassword);
        log.info("Successfully registered new user: {}", request.getEmail());
    }

    public Optional<User> authenticate(LoginRequest request) {
        if (request.getEmail() == null || request.getPassword() == null) {
            return Optional.empty();
        }

        Optional<User> userOpt = userRepository.findByEmail(request.getEmail().trim().toLowerCase());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                log.info("User successfully authenticated: {}", user.getEmail());
                return Optional.of(user);
            }
        }

        log.warn("Authentication failed for email: {}", request.getEmail());
        return Optional.empty();
    }
}
