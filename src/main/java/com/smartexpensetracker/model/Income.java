package com.smartexpensetracker.model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Entity representing an income record.
 */
public class Income implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer incomeId;
    private Integer userId;
    private Double amount;
    private LocalDate incomeDate;
    private String description;

    public Income() {
    }

    public Income(Integer incomeId, Integer userId, Double amount, LocalDate incomeDate, String description) {
        this.incomeId = incomeId;
        this.userId = userId;
        this.amount = amount;
        this.incomeDate = incomeDate;
        this.description = description;
    }

    public Integer getIncomeId() {
        return incomeId;
    }

    public void setIncomeId(Integer incomeId) {
        this.incomeId = incomeId;
    }

    // Alias for template compatibility (row.income_id)
    public Integer getIncome_id() {
        return incomeId;
    }

    public void setIncome_id(Integer incomeId) {
        this.incomeId = incomeId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getIncomeDate() {
        return incomeDate;
    }

    public void setIncomeDate(LocalDate incomeDate) {
        this.incomeDate = incomeDate;
    }

    // Alias for template compatibility (row.income_date)
    public LocalDate getIncome_date() {
        return incomeDate;
    }

    public void setIncome_date(LocalDate incomeDate) {
        this.incomeDate = incomeDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
