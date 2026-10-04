package com.smartexpensetracker.model;

import java.io.Serializable;

/**
 * Budget and alert status model.
 */
public class BudgetSummary implements Serializable {
    private static final long serialVersionUID = 1L;

    private String month;
    private double income;
    private double expense;
    private double savings;
    private double available;
    private String alertType;
    private String message;

    public BudgetSummary() {
    }

    public BudgetSummary(String month, double income, double expense, double savings, double available, String alertType, String message) {
        this.month = month;
        this.income = income;
        this.expense = expense;
        this.savings = savings;
        this.available = available;
        this.alertType = alertType;
        this.message = message;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getExpense() {
        return expense;
    }

    public void setExpense(double expense) {
        this.expense = expense;
    }

    public double getSavings() {
        return savings;
    }

    public void setSavings(double savings) {
        this.savings = savings;
    }

    public double getAvailable() {
        return available;
    }

    public void setAvailable(double available) {
        this.available = available;
    }

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
