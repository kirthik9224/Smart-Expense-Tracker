package com.smartexpensetracker.service;

import com.smartexpensetracker.model.BudgetSummary;
import com.smartexpensetracker.repository.ExpenseRepository;
import com.smartexpensetracker.repository.IncomeRepository;
import com.smartexpensetracker.repository.SavingsRepository;
import org.springframework.stereotype.Service;

import java.time.YearMonth;

/**
 * Service calculating monthly budget status, remaining balances, and alert thresholds.
 */
@Service
public class BudgetService {

    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final SavingsRepository savingsRepository;

    public BudgetService(IncomeRepository incomeRepository,
                         ExpenseRepository expenseRepository,
                         SavingsRepository savingsRepository) {
        this.incomeRepository = incomeRepository;
        this.expenseRepository = expenseRepository;
        this.savingsRepository = savingsRepository;
    }

    public BudgetSummary getBudgetSummary(Integer userId, String month) {
        if (month == null || month.isBlank()) {
            month = YearMonth.now().toString();
        }

        double income = incomeRepository.getTotalIncomeForMonth(userId, month);
        double expense = expenseRepository.getTotalExpenseForMonth(userId, month);
        double savings = savingsRepository.getSavingsForMonth(userId, month);
        double available = income - savings;

        String alertType;
        String message;

        if (income == 0) {
            alertType = "info";
            message = "No income recorded for this month.";
        } else if (expense < available * 0.7) {
            alertType = "success";
            message = "You are managing expenses well. Savings are safe.";
        } else if (expense < available) {
            alertType = "warning";
            message = "You are close to using your savings. Control expenses.";
        } else {
            alertType = "danger";
            message = "You are not able to save money this month.";
        }

        return new BudgetSummary(month, income, expense, savings, available, alertType, message);
    }
}
