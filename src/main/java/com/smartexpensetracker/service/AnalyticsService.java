/*
 * Organization: Orbytes Global Solutions, Owner: Logaraj S, Created at: 2026-10-11
 * Description: Service managing aggregation for charts, spending visualizations, and trends.
 */
package com.smartexpensetracker.service;

import com.smartexpensetracker.model.CategorySummary;
import com.smartexpensetracker.model.MonthlyExpenseSummary;
import com.smartexpensetracker.repository.ExpenseRepository;
import com.smartexpensetracker.repository.IncomeRepository;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service managing data aggregation for charts, spending visualizations, and trends.
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
        List<String> months = expenseRepository.findDistinctMonths(userId);
        if (months == null) {
            months = new ArrayList<>();
        }
        String currentMonth = YearMonth.now().toString();
        if (!months.contains(currentMonth)) {
            months.add(0, currentMonth);
        }
        return months;
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

    public List<String> getRecentMonthsList(int count) {
        List<String> list = new ArrayList<>();
        YearMonth now = YearMonth.now();
        for (int i = count - 1; i >= 0; i--) {
            list.add(now.minusMonths(i).toString());
        }
        return list;
    }

    public List<Double> getTrendIncomes(Integer userId, List<String> months) {
        return months.stream()
                .map(m -> incomeRepository.getTotalIncomeForMonth(userId, m))
                .collect(Collectors.toList());
    }

    public List<Double> getTrendExpenses(Integer userId, List<String> months) {
        return months.stream()
                .map(m -> expenseRepository.getTotalExpenseForMonth(userId, m))
                .collect(Collectors.toList());
    }
}
