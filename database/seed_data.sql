-- =====================================================================
-- Smart Expense Tracker - Seed Data (Optional Test Data)
-- Database: expense_tracker
-- Note: Default demo user password is 'password123'
-- =====================================================================

USE `expense_tracker`;

-- Sample User (Password: password123 encoded with BCrypt)
INSERT INTO `users` (`id`, `name`, `email`, `password`) VALUES
(1, 'Demo User', 'demo@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy')
ON DUPLICATE KEY UPDATE `name` = VALUES(`name`);

-- Sample Income
INSERT INTO `income` (`income_id`, `user_id`, `amount`, `income_date`, `description`) VALUES
(1, 1, 25000.00, CURDATE(), 'Monthly Allowance / Salary'),
(2, 1, 5000.00, CURDATE(), 'Freelance Project')
ON DUPLICATE KEY UPDATE `amount` = VALUES(`amount`);

-- Sample Savings
INSERT INTO `savings` (`savings_id`, `user_id`, `month_year`, `savings_amount`) VALUES
(1, 1, DATE_FORMAT(CURDATE(), '%Y-%m'), 5000.00)
ON DUPLICATE KEY UPDATE `savings_amount` = VALUES(`savings_amount`);

-- Sample Expenses
INSERT INTO `expense` (`expense_id`, `user_id`, `category`, `amount`, `expense_date`, `description`) VALUES
(1, 1, 'Food', 1200.00, CURDATE(), 'Grocery shopping'),
(2, 1, 'Travel', 450.00, CURDATE(), 'Metro recharge'),
(3, 1, 'Entertainment', 600.00, CURDATE(), 'Movie tickets'),
(4, 1, 'Rent', 8000.00, CURDATE(), 'Room rent share')
ON DUPLICATE KEY UPDATE `amount` = VALUES(`amount`);
