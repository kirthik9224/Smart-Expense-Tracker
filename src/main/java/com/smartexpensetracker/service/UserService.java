package com.smartexpensetracker.service;

import com.smartexpensetracker.dto.PasswordChangeRequest;
import com.smartexpensetracker.dto.ProfileUpdateRequest;
import com.smartexpensetracker.exception.ResourceNotFoundException;
import com.smartexpensetracker.exception.ValidationException;
import com.smartexpensetracker.model.User;
import com.smartexpensetracker.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service managing user profile and account operations.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }

    public void updateProfile(Integer userId, ProfileUpdateRequest request) {
        String email = request.getEmail().trim().toLowerCase();
        if (userRepository.existsByEmailAndNotId(email, userId)) {
            throw new ValidationException("Email is already used by another account.");
        }
        userRepository.updateProfile(userId, request.getName().trim(), email);
    }

    public void changePassword(Integer userId, PasswordChangeRequest request) {
        User user = getUserById(userId);

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new ValidationException("Current password is incorrect.");
        }
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new ValidationException("New password and confirmation do not match.");
        }
        if (request.getNewPassword().length() < 6) {
            throw new ValidationException("New password must contain at least 6 characters.");
        }

        String encoded = passwordEncoder.encode(request.getNewPassword());
        userRepository.updatePassword(userId, encoded);
    }
}
