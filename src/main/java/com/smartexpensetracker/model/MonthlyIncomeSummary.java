package com.smartexpensetracker.model;

import java.io.Serializable;

/**
 * Summary DTO / View model for month-wise income aggregation with savings.
 */
public class MonthlyIncomeSummary implements Serializable {
    private static final long serialVersionUID = 1L;

    private String month;
    private Double totalIncome;
    private Double savings;

    public MonthlyIncomeSummary() {
    }

    public MonthlyIncomeSummary(String month, Double totalIncome, Double savings) {
        this.month = month;
        this.totalIncome = totalIncome;
        this.savings = savings;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(Double totalIncome) {
        this.totalIncome = totalIncome;
    }

    // Alias for template compatibility (m.total_income)
    public Double getTotal_income() {
        return totalIncome;
    }

    public void setTotal_income(Double totalIncome) {
        this.totalIncome = totalIncome;
    }

    public Double getSavings() {
        return savings;
    }

    public void setSavings(Double savings) {
        this.savings = savings;
    }
}
