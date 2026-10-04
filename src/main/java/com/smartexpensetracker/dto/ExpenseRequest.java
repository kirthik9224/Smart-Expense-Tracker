package com.smartexpensetracker.dto;

import java.time.LocalDate;

/**
 * Data Transfer Object for creating or updating an expense entry.
 */
public class ExpenseRequest {
    private Double amount;
    private LocalDate date;
    private String category;
    private String description;

    public ExpenseRequest() {
    }

    public ExpenseRequest(Double amount, LocalDate date, String category, String description) {
        this.amount = amount;
        this.date = date;
        this.category = category;
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
