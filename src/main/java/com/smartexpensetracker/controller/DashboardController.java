/*
 * Organization: Orbytes Global Solutions, Owner: Logaraj S, Created at: 2026-10-11
 * Description: Controller handling comprehensive interactive user dashboard overview.
 */
package com.smartexpensetracker.controller;

import com.smartexpensetracker.model.BudgetSummary;
import com.smartexpensetracker.model.CategorySummary;
import com.smartexpensetracker.model.Expense;
import com.smartexpensetracker.service.AnalyticsService;
import com.smartexpensetracker.service.BudgetService;
import com.smartexpensetracker.service.ExpenseService;
import com.smartexpensetracker.service.IncomeService;
import com.smartexpensetracker.service.SavingsService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller handling user dashboard overview and interactive analytics.
 */
@Controller
public class DashboardController {

    private final ExpenseService expenseService;
    private final IncomeService incomeService;
    private final BudgetService budgetService;
    private final AnalyticsService analyticsService;
    private final SavingsService savingsService;

    public DashboardController(ExpenseService expenseService,
                               IncomeService incomeService,
                               BudgetService budgetService,
                               AnalyticsService analyticsService,
                               SavingsService savingsService) {
        this.expenseService = expenseService;
        this.incomeService = incomeService;
        this.budgetService = budgetService;
        this.analyticsService = analyticsService;
        this.savingsService = savingsService;
    }

    private Integer getAuthenticatedUserId(HttpSession session) {
        Object uid = session.getAttribute("user_id");
        return uid != null ? ((Number) uid).intValue() : null;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Integer userId = getAuthenticatedUserId(session);
        if (userId == null) {
            return "redirect:/login";
        }

        String currentMonth = YearMonth.now().toString();
        double totalIncome = incomeService.getTotalIncomeForMonth(userId, currentMonth);
        double totalExpense = expenseService.getTotalExpenseForMonth(userId, currentMonth);
        double savingsGoal = savingsService.getSavingsForMonth(userId, currentMonth);
        double netBalance = totalIncome - totalExpense;

        BudgetSummary budgetSummary = budgetService.getBudgetSummary(userId, currentMonth);

        List<Expense> allExpenses = expenseService.getExpenseHistory(userId);
        List<Expense> recentExpenses = allExpenses.stream().limit(6).collect(Collectors.toList());

        List<CategorySummary> categorySummaries =
                analyticsService.getCategorySummaries(userId, currentMonth);
        List<String> categoryLabels = analyticsService.getCategoryLabels(userId, currentMonth);
        List<Double> categoryValues = analyticsService.getCategoryValues(userId, currentMonth);

        double savingsRate = 0.0;
        if (totalIncome > 0) {
            savingsRate = Math.max(0.0, (netBalance / totalIncome) * 100.0);
        }

        model.addAttribute("currentMonth", currentMonth);
        model.addAttribute("totalIncome", totalIncome);
        model.addAttribute("totalExpense", totalExpense);
        model.addAttribute("savingsGoal", savingsGoal);
        model.addAttribute("netBalance", netBalance);
        model.addAttribute("savingsRate", Math.round(savingsRate));
        model.addAttribute("budgetSummary", budgetSummary);
        model.addAttribute("recentExpenses", recentExpenses);
        model.addAttribute("categorySummaries", categorySummaries);
        model.addAttribute("categoryLabels", categoryLabels);
        model.addAttribute("categoryValues", categoryValues);

        return "dashboard";
    }
}
