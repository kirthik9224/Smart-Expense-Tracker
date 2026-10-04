package com.smartexpensetracker.repository;

import com.smartexpensetracker.model.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Data access repository for User entities.
 */
@Repository
public class UserRepository {

    private final JdbcTemplate db;

    public UserRepository(JdbcTemplate db) {
        this.db = db;
    }

    private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        return user;
    };

    public void save(String name, String email, String encodedPassword) {
        String sql = "INSERT INTO users(name, email, password) VALUES(?, ?, ?)";
        db.update(sql, name, email, encodedPassword);
    }

    public Optional<User> findByEmail(String email) {
        String sql = "SELECT id, name, email, password FROM users WHERE email = ?";
        List<User> results = db.query(sql, userRowMapper, email);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public Optional<User> findById(Integer id) {
        String sql = "SELECT id, name, email, password FROM users WHERE id = ?";
        List<User> results = db.query(sql, userRowMapper, id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        Integer count = db.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    public boolean existsByEmailAndNotId(String email, Integer id) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ? AND id <> ?";
        Integer count = db.queryForObject(sql, Integer.class, email, id);
        return count != null && count > 0;
    }

    public void updateProfile(Integer id, String name, String email) {
        String sql = "UPDATE users SET name = ?, email = ? WHERE id = ?";
        db.update(sql, name, email, id);
    }

    public void updatePassword(Integer id, String encodedPassword) {
        String sql = "UPDATE users SET password = ? WHERE id = ?";
        db.update(sql, encodedPassword, id);
    }
}
