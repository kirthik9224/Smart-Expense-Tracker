package com.smartexpensetracker.controller;

import com.smartexpensetracker.dto.PasswordChangeRequest;
import com.smartexpensetracker.dto.ProfileUpdateRequest;
import com.smartexpensetracker.exception.ValidationException;
import com.smartexpensetracker.model.User;
import com.smartexpensetracker.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller managing account settings, profile updates, and password changes.
 */
@Controller
public class SettingsController {

    private final UserService userService;

    public SettingsController(UserService userService) {
        this.userService = userService;
    }

    private Integer getAuthenticatedUserId(HttpSession session) {
        Object uid = session.getAttribute("user_id");
        return uid != null ? ((Number) uid).intValue() : null;
    }

    @GetMapping("/settings")
    public String settings(HttpSession session, Model model) {
        Integer userId = getAuthenticatedUserId(session);
        if (userId == null) {
            return "redirect:/login";
        }

        User user = userService.getUserById(userId);
        model.addAttribute("user", user);
        return "settings";
    }

    @PostMapping("/settings/profile")
    public String updateProfile(@RequestParam String name,
                                @RequestParam String email,
                                HttpSession session,
                                Model model) {
        Integer userId = getAuthenticatedUserId(session);
        if (userId == null) {
            return "redirect:/login";
        }

        try {
            ProfileUpdateRequest request = new ProfileUpdateRequest(name, email);
            userService.updateProfile(userId, request);
            session.setAttribute("user_name", name.trim());
            model.addAttribute("message", "Profile updated successfully.");
        } catch (ValidationException e) {
            model.addAttribute("error", e.getMessage());
        }

        User user = userService.getUserById(userId);
        model.addAttribute("user", user);
        return "settings";
    }

    @PostMapping("/settings/password")
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 HttpSession session,
                                 Model model) {
        Integer userId = getAuthenticatedUserId(session);
        if (userId == null) {
            return "redirect:/login";
        }

        try {
            PasswordChangeRequest request = new PasswordChangeRequest(currentPassword, newPassword, confirmPassword);
            userService.changePassword(userId, request);
            model.addAttribute("message", "Password changed successfully.");
        } catch (ValidationException e) {
            model.addAttribute("error", e.getMessage());
        }

        User user = userService.getUserById(userId);
        model.addAttribute("user", user);
        return "settings";
    }
}
