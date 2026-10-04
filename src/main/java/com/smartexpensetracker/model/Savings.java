package com.smartexpensetracker.model;

import java.io.Serializable;

/**
 * Entity representing monthly savings target/allocation.
 */
public class Savings implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer savingsId;
    private Integer userId;
    private String monthYear;
    private Double savingsAmount;

    public Savings() {
    }

    public Savings(Integer savingsId, Integer userId, String monthYear, Double savingsAmount) {
        this.savingsId = savingsId;
        this.userId = userId;
        this.monthYear = monthYear;
        this.savingsAmount = savingsAmount;
    }

    public Integer getSavingsId() {
        return savingsId;
    }

    public void setSavingsId(Integer savingsId) {
        this.savingsId = savingsId;
    }

    public Integer getSavings_id() {
        return savingsId;
    }

    public void setSavings_id(Integer savingsId) {
        this.savingsId = savingsId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    public String getMonth_year() {
        return monthYear;
    }

    public void setMonth_year(String monthYear) {
        this.monthYear = monthYear;
    }

    public Double getSavingsAmount() {
        return savingsAmount;
    }

    public void setSavingsAmount(Double savingsAmount) {
        this.savingsAmount = savingsAmount;
    }

    public Double getSavings_amount() {
        return savingsAmount;
    }

    public void setSavings_amount(Double savingsAmount) {
        this.savingsAmount = savingsAmount;
    }
}
