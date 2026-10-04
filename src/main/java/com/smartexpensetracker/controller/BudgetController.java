package com.smartexpensetracker.controller;

import com.smartexpensetracker.model.BudgetSummary;
import com.smartexpensetracker.service.BudgetService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.YearMonth;

/**
 * Controller managing monthly budget limits, savings health, and alerts.
 */
@Controller
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    private Integer getAuthenticatedUserId(HttpSession session) {
        Object uid = session.getAttribute("user_id");
        return uid != null ? ((Number) uid).intValue() : null;
    }

    @GetMapping("/budget")
    public String budget(HttpSession session, Model model) {
        Integer userId = getAuthenticatedUserId(session);
        if (userId == null) {
            return "redirect:/login";
        }

        String currentMonth = YearMonth.now().toString();
        BudgetSummary summary = budgetService.getBudgetSummary(userId, currentMonth);

        model.addAttribute("income", summary.getIncome());
        model.addAttribute("expense", summary.getExpense());
        model.addAttribute("savings", summary.getSavings());
        model.addAttribute("available", summary.getAvailable());
        model.addAttribute("alertType", summary.getAlertType());
        model.addAttribute("message", summary.getMessage());
        model.addAttribute("month", summary.getMonth());

        return "budget";
    }
}
