/*
 * Organization: Orbytes Global Solutions, Owner: Logaraj S, Created at: 2026-10-11
 * Description: Controller managing visual expense evaluations, category charts, and trends.
 */
package com.smartexpensetracker.controller;

import com.smartexpensetracker.model.CategorySummary;
import com.smartexpensetracker.service.AnalyticsService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.YearMonth;
import java.util.Collections;
import java.util.List;

/**
 * Controller managing visual expense evaluations, category distributions, and trend charts.
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
                ? (months.isEmpty() ? YearMonth.now().toString() : months.get(0))
                : month;

        double income = analyticsService.getMonthlyIncome(userId, selectedMonth);
        double expense = analyticsService.getMonthlyExpense(userId, selectedMonth);
        List<CategorySummary> categorySummaries =
                analyticsService.getCategorySummaries(userId, selectedMonth);
        List<String> categoryLabels = analyticsService.getCategoryLabels(userId, selectedMonth);
        List<Double> categoryValues = analyticsService.getCategoryValues(userId, selectedMonth);

        double balance = income - expense;

        List<String> trendMonths = analyticsService.getRecentMonthsList(6);
        List<Double> trendIncomes = analyticsService.getTrendIncomes(userId, trendMonths);
        List<Double> trendExpenses = analyticsService.getTrendExpenses(userId, trendMonths);

        model.addAttribute("months", months);
        model.addAttribute("selectedMonth", selectedMonth);
        model.addAttribute("monthlyIncome", income);
        model.addAttribute("totalExpense", expense);
        model.addAttribute("balance", balance);
        model.addAttribute("categorySummaries", categorySummaries);
        model.addAttribute("categoryLabels", categoryLabels);
        model.addAttribute("categoryValues", categoryValues);
        model.addAttribute("trendMonths", trendMonths);
        model.addAttribute("trendIncomes", trendIncomes);
        model.addAttribute("trendExpenses", trendExpenses);

        return "expense_evaluation";
    }
}
