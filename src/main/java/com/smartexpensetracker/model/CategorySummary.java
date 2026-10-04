package com.smartexpensetracker.model;

import java.io.Serializable;

/**
 * Category breakdown model for expense evaluations and analytics charts.
 */
public class CategorySummary implements Serializable {
    private static final long serialVersionUID = 1L;

    private String category;
    private Double total;

    public CategorySummary() {
    }

    public CategorySummary(String category, Double total) {
        this.category = category;
        this.total = total;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
