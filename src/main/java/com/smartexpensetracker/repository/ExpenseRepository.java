package com.smartexpensetracker.repository;

import com.smartexpensetracker.model.CategorySummary;
import com.smartexpensetracker.model.Expense;
import com.smartexpensetracker.model.MonthlyExpenseSummary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Data access repository for Expense entities.
 */
@Repository
public class ExpenseRepository {

    private final JdbcTemplate db;

    public ExpenseRepository(JdbcTemplate db) {
        this.db = db;
    }

    private final RowMapper<Expense> expenseRowMapper = (rs, rowNum) -> {
        Expense expense = new Expense();
        expense.setExpenseId(rs.getInt("expense_id"));
        expense.setUserId(rs.getInt("user_id"));
        expense.setCategory(rs.getString("category"));
        expense.setAmount(rs.getDouble("amount"));
        Date d = rs.getDate("expense_date");
        if (d != null) {
            expense.setExpenseDate(d.toLocalDate());
        }
        expense.setDescription(rs.getString("description"));
        return expense;
    };

    public void save(Integer userId, String category, Double amount, LocalDate expenseDate, String description) {
        String sql = "INSERT INTO expense(user_id, category, amount, expense_date, description) VALUES(?, ?, ?, ?, ?)";
        db.update(sql, userId, category, amount, Date.valueOf(expenseDate), description);
    }

    public List<Expense> findByUserIdOrderByDateDesc(Integer userId) {
        String sql = "SELECT expense_id, user_id, category, amount, expense_date, description FROM expense WHERE user_id = ? ORDER BY expense_date DESC";
        return db.query(sql, expenseRowMapper, userId);
    }

    public Optional<Expense> findById(Integer expenseId) {
        String sql = "SELECT expense_id, user_id, category, amount, expense_date, description FROM expense WHERE expense_id = ?";
        List<Expense> results = db.query(sql, expenseRowMapper, expenseId);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public void update(Integer expenseId, Double amount, LocalDate expenseDate, String category, String description) {
        String sql = "UPDATE expense SET amount = ?, expense_date = ?, category = ?, description = ? WHERE expense_id = ?";
        db.update(sql, amount, Date.valueOf(expenseDate), category, description, expenseId);
    }

    public void deleteById(Integer expenseId) {
        String sql = "DELETE FROM expense WHERE expense_id = ?";
        db.update(sql, expenseId);
    }

    public List<MonthlyExpenseSummary> findMonthlyExpenseSummary(Integer userId) {
        String sql = "SELECT DATE_FORMAT(expense_date, '%Y-%m') AS month, SUM(amount) AS total_expense " +
                     "FROM expense WHERE user_id = ? GROUP BY month ORDER BY month DESC";
        return db.query(sql, (rs, rowNum) -> new MonthlyExpenseSummary(
                rs.getString("month"),
                rs.getDouble("total_expense")
        ), userId);
    }

    public double getTotalExpenseForMonth(Integer userId, String monthYear) {
        String sql = "SELECT IFNULL(SUM(amount), 0) FROM expense WHERE user_id = ? AND DATE_FORMAT(expense_date, '%Y-%m') = ?";
        Double total = db.queryForObject(sql, Double.class, userId, monthYear);
        return total != null ? total : 0.0;
    }

    public List<String> findDistinctMonths(Integer userId) {
        String sql = "SELECT DISTINCT DATE_FORMAT(expense_date, '%Y-%m') FROM expense WHERE user_id = ? ORDER BY 1 DESC";
        return db.queryForList(sql, String.class, userId);
    }

    public List<CategorySummary> findCategorySummariesForMonth(Integer userId, String monthYear) {
        String sql = "SELECT category, SUM(amount) AS total FROM expense " +
                     "WHERE user_id = ? AND DATE_FORMAT(expense_date, '%Y-%m') = ? " +
                     "GROUP BY category";
        return db.query(sql, (rs, rowNum) -> new CategorySummary(
                rs.getString("category"),
                rs.getDouble("total")
        ), userId, monthYear);
    }
}
