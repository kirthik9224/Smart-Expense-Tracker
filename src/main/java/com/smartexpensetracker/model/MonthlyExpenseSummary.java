package com.smartexpensetracker.model;

import java.io.Serializable;

/**
 * Summary DTO / View model for month-wise total expense aggregation.
 */
public class MonthlyExpenseSummary implements Serializable {
    private static final long serialVersionUID = 1L;

    private String month;
    private Double totalExpense;

    public MonthlyExpenseSummary() {
    }

    public MonthlyExpenseSummary(String month, Double totalExpense) {
        this.month = month;
        this.totalExpense = totalExpense;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Double getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(Double totalExpense) {
        this.totalExpense = totalExpense;
    }

    // Alias for template compatibility (m.total_expense)
    public Double getTotal_expense() {
        return totalExpense;
    }

    public void setTotal_expense(Double totalExpense) {
        this.totalExpense = totalExpense;
    }
}
