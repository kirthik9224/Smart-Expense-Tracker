package com.smartexpensetracker.model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Entity representing an expense record.
 */
public class Expense implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer expenseId;
    private Integer userId;
    private String category;
    private Double amount;
    private LocalDate expenseDate;
    private String description;

    public Expense() {
    }

    public Expense(Integer expenseId, Integer userId, String category, Double amount, LocalDate expenseDate, String description) {
        this.expenseId = expenseId;
        this.userId = userId;
        this.category = category;
        this.amount = amount;
        this.expenseDate = expenseDate;
        this.description = description;
    }

    public Integer getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(Integer expenseId) {
        this.expenseId = expenseId;
    }

    // Alias for template compatibility (e.expense_id)
    public Integer getExpense_id() {
        return expenseId;
    }

    public void setExpense_id(Integer expenseId) {
        this.expenseId = expenseId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(LocalDate expenseDate) {
        this.expenseDate = expenseDate;
    }

    // Alias for template compatibility (e.expense_date)
    public LocalDate getExpense_date() {
        return expenseDate;
    }

    public void setExpense_date(LocalDate expenseDate) {
        this.expenseDate = expenseDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
