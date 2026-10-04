package com.smartexpensetracker.repository;

import com.smartexpensetracker.model.Savings;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Data access repository for Savings entities.
 */
@Repository
public class SavingsRepository {

    private final JdbcTemplate db;

    public SavingsRepository(JdbcTemplate db) {
        this.db = db;
    }

    private final RowMapper<Savings> savingsRowMapper = (rs, rowNum) -> {
        Savings savings = new Savings();
        savings.setSavingsId(rs.getInt("savings_id"));
        savings.setUserId(rs.getInt("user_id"));
        savings.setMonthYear(rs.getString("month_year"));
        savings.setSavingsAmount(rs.getDouble("savings_amount"));
        return savings;
    };

    public Optional<Savings> findByUserIdAndMonth(Integer userId, String monthYear) {
        String sql = "SELECT savings_id, user_id, month_year, savings_amount FROM savings WHERE user_id = ? AND month_year = ?";
        List<Savings> list = db.query(sql, savingsRowMapper, userId, monthYear);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    public void saveOrUpdate(Integer userId, String monthYear, Double amount) {
        String countSql = "SELECT COUNT(*) FROM savings WHERE user_id = ? AND month_year = ?";
        Integer count = db.queryForObject(countSql, Integer.class, userId, monthYear);
        if (count != null && count > 0) {
            String updateSql = "UPDATE savings SET savings_amount = ? WHERE user_id = ? AND month_year = ?";
            db.update(updateSql, amount, userId, monthYear);
        } else {
            String insertSql = "INSERT INTO savings(user_id, month_year, savings_amount) VALUES(?, ?, ?)";
            db.update(insertSql, userId, monthYear, amount);
        }
    }

    public double getSavingsForMonth(Integer userId, String monthYear) {
        String sql = "SELECT IFNULL(MAX(savings_amount), 0) FROM savings WHERE user_id = ? AND month_year = ?";
        Double amount = db.queryForObject(sql, Double.class, userId, monthYear);
        return amount != null ? amount : 0.0;
    }
}
