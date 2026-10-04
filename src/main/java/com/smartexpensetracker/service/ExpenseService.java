package com.smartexpensetracker.service;

import com.smartexpensetracker.dto.ExpenseRequest;
import com.smartexpensetracker.exception.ResourceNotFoundException;
import com.smartexpensetracker.model.Expense;
import com.smartexpensetracker.model.MonthlyExpenseSummary;
import com.smartexpensetracker.repository.ExpenseRepository;
import com.smartexpensetracker.repository.IncomeRepository;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Service managing user expense operations, validation against monthly income, and summaries.
 */
@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final IncomeRepository incomeRepository;

    public ExpenseService(ExpenseRepository expenseRepository, IncomeRepository incomeRepository) {
        this.expenseRepository = expenseRepository;
        this.incomeRepository = incomeRepository;
    }

    public String addExpense(Integer userId, ExpenseRequest request) {
        String month = request.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        double monthlyIncome = incomeRepository.getTotalIncomeForMonth(userId, month);
        double currentMonthlyExpense = expenseRepository.getTotalExpenseForMonth(userId, month);

        if (currentMonthlyExpense + request.getAmount() > monthlyIncome) {
            return "Expense exceeds total income for this month!";
        }

        expenseRepository.save(
                userId,
                request.getCategory(),
                request.getAmount(),
                request.getDate(),
                request.getDescription()
        );
        return "Expense added successfully!";
    }

    public List<Expense> getExpenseHistory(Integer userId) {
        return expenseRepository.findByUserIdOrderByDateDesc(userId);
    }

    public Expense getExpenseById(Integer expenseId) {
        return expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense record not found with ID: " + expenseId));
    }

    public void updateExpense(Integer expenseId, ExpenseRequest request) {
        expenseRepository.update(
                expenseId,
                request.getAmount(),
                request.getDate(),
                request.getCategory(),
                request.getDescription()
        );
    }

    public void deleteExpense(Integer expenseId) {
        expenseRepository.deleteById(expenseId);
    }

    public List<MonthlyExpenseSummary> getMonthlyExpenses(Integer userId) {
        return expenseRepository.findMonthlyExpenseSummary(userId);
    }

    public double getTotalExpenseForMonth(Integer userId, String monthYear) {
        return expenseRepository.getTotalExpenseForMonth(userId, monthYear);
    }
}
