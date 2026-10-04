package com.smartexpensetracker.repository;

import com.smartexpensetracker.model.Income;
import com.smartexpensetracker.model.MonthlyIncomeSummary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Data access repository for Income entities.
 */
@Repository
public class IncomeRepository {

    private final JdbcTemplate db;

    public IncomeRepository(JdbcTemplate db) {
        this.db = db;
    }

    private final RowMapper<Income> incomeRowMapper = (rs, rowNum) -> {
        Income income = new Income();
        income.setIncomeId(rs.getInt("income_id"));
        income.setUserId(rs.getInt("user_id"));
        income.setAmount(rs.getDouble("amount"));
        Date d = rs.getDate("income_date");
        if (d != null) {
            income.setIncomeDate(d.toLocalDate());
        }
        income.setDescription(rs.getString("description"));
        return income;
    };

    public void save(Integer userId, Double amount, LocalDate incomeDate, String description) {
        String sql = "INSERT INTO income(user_id, amount, income_date, description) VALUES(?, ?, ?, ?)";
        db.update(sql, userId, amount, Date.valueOf(incomeDate), description);
    }

    public List<Income> findByUserIdOrderByDateDesc(Integer userId) {
        String sql = "SELECT income_id, user_id, amount, income_date, description FROM income WHERE user_id = ? ORDER BY income_date DESC";
        return db.query(sql, incomeRowMapper, userId);
    }

    public Optional<Income> findById(Integer incomeId) {
        String sql = "SELECT income_id, user_id, amount, income_date, description FROM income WHERE income_id = ?";
        List<Income> results = db.query(sql, incomeRowMapper, incomeId);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public void update(Integer incomeId, Double amount, LocalDate incomeDate, String description) {
        String sql = "UPDATE income SET amount = ?, income_date = ?, description = ? WHERE income_id = ?";
        db.update(sql, amount, Date.valueOf(incomeDate), description, incomeId);
    }

    public void deleteById(Integer incomeId) {
        String sql = "DELETE FROM income WHERE income_id = ?";
        db.update(sql, incomeId);
    }

    public List<MonthlyIncomeSummary> findMonthlyIncomeWithSavings(Integer userId) {
        String sql = "SELECT DATE_FORMAT(i.income_date, '%Y-%m') AS month, " +
                     "SUM(i.amount) AS total_income, " +
                     "IFNULL(MAX(s.savings_amount), 0) AS savings " +
                     "FROM income i " +
                     "LEFT JOIN savings s ON s.user_id = i.user_id AND s.month_year = DATE_FORMAT(i.income_date, '%Y-%m') " +
                     "WHERE i.user_id = ? " +
                     "GROUP BY month " +
                     "ORDER BY month DESC";

        return db.query(sql, (rs, rowNum) -> new MonthlyIncomeSummary(
                rs.getString("month"),
                rs.getDouble("total_income"),
                rs.getDouble("savings")
        ), userId);
    }

    public double getTotalIncomeForMonth(Integer userId, String monthYear) {
        String sql = "SELECT IFNULL(SUM(amount), 0) FROM income WHERE user_id = ? AND DATE_FORMAT(income_date, '%Y-%m') = ?";
        Double total = db.queryForObject(sql, Double.class, userId, monthYear);
        return total != null ? total : 0.0;
    }
}
