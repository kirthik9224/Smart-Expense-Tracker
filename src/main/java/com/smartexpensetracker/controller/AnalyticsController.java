package com.smartexpensetracker.controller;

import com.smartexpensetracker.service.AnalyticsService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

/**
 * Controller managing expense evaluations, category distributions, and analytics charts.
 */
@Controller
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    private Integer getAuthenticatedUserId(HttpSession session) {
        Object uid = session.getAttribute("user_id");
        return uid != null ? ((Number) uid).intValue() : null;
    }

    @GetMapping("/expense_evaluation")
    public String evaluation(@RequestParam(required = false) String month,
                             HttpSession session,
                             Model model) {
        Integer userId = getAuthenticatedUserId(session);
        if (userId == null) {
            return "redirect:/login";
        }

        List<String> months = analyticsService.getAvailableMonths(userId);
        String selectedMonth = (month == null || month.isBlank())
                ? (months.isEmpty() ? null : months.get(0))
                : month;

        double income = 0.0;
        double expense = 0.0;
        List<String> categoryLabels = Collections.emptyList();
        List<Double> categoryValues = Collections.emptyList();

        if (selectedMonth != null) {
            income = analyticsService.getMonthlyIncome(userId, selectedMonth);
            expense = analyticsService.getMonthlyExpense(userId, selectedMonth);
            categoryLabels = analyticsService.getCategoryLabels(userId, selectedMonth);
            categoryValues = analyticsService.getCategoryValues(userId, selectedMonth);
        }

        double balance = income - expense;

        model.addAttribute("months", months);
        model.addAttribute("selectedMonth", selectedMonth);
        model.addAttribute("totalExpense", expense);
        model.addAttribute("balance", balance);
        model.addAttribute("categoryLabels", categoryLabels);
        model.addAttribute("categoryValues", categoryValues);

        return "expense_evaluation";
    }
}
