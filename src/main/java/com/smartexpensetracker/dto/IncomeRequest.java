package com.smartexpensetracker.dto;

import java.time.LocalDate;

/**
 * Data Transfer Object for creating or updating an income entry.
 */
public class IncomeRequest {
    private Double amount;
    private LocalDate date;
    private String description;

    public IncomeRequest() {
    }

    public IncomeRequest(Double amount, LocalDate date, String description) {
        this.amount = amount;
        this.date = date;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
