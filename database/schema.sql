-- =====================================================================
-- Smart Expense Tracker - Database Schema Definition
-- Database: expense_tracker
-- Compatibility: MySQL 8.0+ / MariaDB 10.4+
-- =====================================================================

CREATE DATABASE IF NOT EXISTS `expense_tracker` 
DEFAULT CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE `expense_tracker`;

-- ---------------------------------------------------------------------
-- Table: users
-- Description: Stores user account details and BCrypt hashed passwords
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `users` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL,
    `email` VARCHAR(100) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ---------------------------------------------------------------------
-- Table: income
-- Description: Stores user income records by date
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `income` (
    `income_id` INT NOT NULL AUTO_INCREMENT,
    `user_id` INT NOT NULL,
    `amount` DECIMAL(12, 2) NOT NULL,
    `income_date` DATE NOT NULL,
    `description` VARCHAR(255) DEFAULT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`income_id`),
    KEY `idx_income_user_date` (`user_id`, `income_date`),
    CONSTRAINT `fk_income_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ---------------------------------------------------------------------
-- Table: expense
-- Description: Stores user categorized expense entries
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `expense` (
    `expense_id` INT NOT NULL AUTO_INCREMENT,
    `user_id` INT NOT NULL,
    `category` VARCHAR(50) NOT NULL,
    `amount` DECIMAL(12, 2) NOT NULL,
    `expense_date` DATE NOT NULL,
    `description` VARCHAR(255) DEFAULT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`expense_id`),
    KEY `idx_expense_user_date` (`user_id`, `expense_date`),
    KEY `idx_expense_category` (`category`),
    CONSTRAINT `fk_expense_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ---------------------------------------------------------------------
-- Table: savings
-- Description: Stores monthly savings goals/amounts set by users
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `savings` (
    `savings_id` INT NOT NULL AUTO_INCREMENT,
    `user_id` INT NOT NULL,
    `month_year` VARCHAR(20) NOT NULL,
    `savings_amount` DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`savings_id`),
    UNIQUE KEY `uk_user_month` (`user_id`, `month_year`),
    CONSTRAINT `fk_savings_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
