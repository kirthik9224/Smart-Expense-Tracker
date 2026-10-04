package com.smartexpensetracker.service;

import com.smartexpensetracker.model.CategorySummary;
import com.smartexpensetracker.repository.ExpenseRepository;
import com.smartexpensetracker.repository.IncomeRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service managing data aggregation for charts, spending visualizations, and expense evaluation.
 */
@Service
public class AnalyticsService {

    private final ExpenseRepository expenseRepository;
    private final IncomeRepository incomeRepository;

    public AnalyticsService(ExpenseRepository expenseRepository, IncomeRepository incomeRepository) {
        this.expenseRepository = expenseRepository;
        this.incomeRepository = incomeRepository;
    }

    public List<String> getAvailableMonths(Integer userId) {
        return expenseRepository.findDistinctMonths(userId);
    }

    public double getMonthlyIncome(Integer userId, String month) {
        if (month == null || month.isBlank()) {
            return 0.0;
        }
        return incomeRepository.getTotalIncomeForMonth(userId, month);
    }

    public double getMonthlyExpense(Integer userId, String month) {
        if (month == null || month.isBlank()) {
            return 0.0;
        }
        return expenseRepository.getTotalExpenseForMonth(userId, month);
    }

    public List<CategorySummary> getCategorySummaries(Integer userId, String month) {
        if (month == null || month.isBlank()) {
            return Collections.emptyList();
        }
        return expenseRepository.findCategorySummariesForMonth(userId, month);
    }

    public List<String> getCategoryLabels(Integer userId, String month) {
        return getCategorySummaries(userId, month).stream()
                .map(CategorySummary::getCategory)
                .collect(Collectors.toList());
    }

    public List<Double> getCategoryValues(Integer userId, String month) {
        return getCategorySummaries(userId, month).stream()
                .map(CategorySummary::getTotal)
                .collect(Collectors.toList());
    }
}
