# 💰 Smart Expense Tracker — Spring Boot Enterprise Edition

[![Java](https://img.shields.io/badge/Java-21-orange.svg?style=flat-square&logo=openjdk)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.0-brightgreen.svg?style=flat-square&logo=springboot)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue.svg?style=flat-square&logo=mysql)](https://www.mysql.com/)
[![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.x-005F0F.svg?style=flat-square&logo=thymeleaf)](https://www.thymeleaf.org/)
[![Maven](https://img.shields.io/badge/Maven-3.9+-C71A36.svg?style=flat-square&logo=apachemaven)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg?style=flat-square)](LICENSE)

A modern, production-ready web application designed for students and professionals to track daily expenses, plan monthly budgets, manage savings targets, and analyze spending patterns with interactive visual analytics.

---

## 📑 Table of Contents

- [Key Features](#-key-features)
- [Architecture & Design](#-architecture--design)
- [Project Directory Structure](#-project-directory-structure)
- [Tech Stack](#-tech-stack)
- [Prerequisites](#-prerequisites)
- [Database Setup](#-database-setup)
- [Configuration](#-configuration)
- [Build & Run](#-build--run)
- [Application Endpoints](#-application-endpoints)
- [Contributing](#-contributing)

---

## 🌟 Key Features

- 🔐 **Secure Authentication & Account Management**: User registration, session-based login, password hashing with BCrypt, profile editing, and secure password updates.
- 💵 **Income & Savings Management**: Track multiple income sources by date, view month-wise earnings, and set monthly savings goals.
- 📊 **Smart Expense Tracking & Guardrails**: Record categorized expenses (Food, Travel, Rent, Entertainment, Others), with automated guardrails warning users when expenses exceed monthly income.
- 📈 **Visual Spending Analytics**: Interactive Pie and Bar charts powered by Chart.js for evaluating category distribution and budget balance.
- 🚨 **Intelligent Budget Alerts**: Automatic calculation of financial health statuses (Safe, Warning, Danger) based on savings retention and expense ratios.
- 📱 **Modern Responsive UI**: Clean, mobile-friendly interface built with modern CSS and FontAwesome icons.

---

## 🏛️ Architecture & Design

The project strictly adheres to the **Layered Clean Architecture** pattern, ensuring separation of concerns, testability, and maintainability:

```mermaid
flowchart TD
    Browser["Client / Web Browser"]
    Controller["Controller Layer (Web / MVC)"]
    Service["Service Layer (Business Logic)"]
    Repository["Repository Layer (JdbcTemplate / Data Access)"]
    Database[("MySQL Database")]

    Browser <-->|HTTP / HTML (Thymeleaf)| Controller
    Controller <-->|DTOs / Domain Models| Service
    Service <-->|Entities / Mappers| Repository
    Repository <-->|Parameterized SQL| Database
```

---

## 📁 Project Directory Structure

```
Smart expense tracker java/
├── database/                                # Database schemas & seed scripts
│   ├── schema.sql                           # Production DDL schema with constraints
│   ├── seed_data.sql                        # Optional development seed data
│   └── expense_tracker.sql                  # Legacy database dump
├── src/
│   ├── main/
│   │   ├── java/com/smartexpensetracker/
│   │   │   ├── SmartExpenseTrackerApplication.java  # Main entry point
│   │   │   ├── config/                     # Application configurations (Beans, Security)
│   │   │   │   └── AppConfig.java
│   │   │   ├── controller/                 # Web MVC Controllers
│   │   │   │   ├── AnalyticsController.java
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── BudgetController.java
│   │   │   │   ├── DashboardController.java
│   │   │   │   ├── ExpenseController.java
│   │   │   │   ├── IncomeController.java
│   │   │   │   └── SettingsController.java
│   │   │   ├── dto/                        # Data Transfer Objects
│   │   │   │   ├── ExpenseRequest.java
│   │   │   │   ├── IncomeRequest.java
│   │   │   │   ├── LoginRequest.java
│   │   │   │   ├── PasswordChangeRequest.java
│   │   │   │   ├── ProfileUpdateRequest.java
│   │   │   │   └── RegisterRequest.java
│   │   │   ├── exception/                  # Global & Custom Exception Handlers
│   │   │   │   ├── AppException.java
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   └── ValidationException.java
│   │   │   ├── model/                      # Domain Entities & View Models
│   │   │   │   ├── BudgetSummary.java
│   │   │   │   ├── CategorySummary.java
│   │   │   │   ├── Expense.java
│   │   │   │   ├── Income.java
│   │   │   │   ├── MonthlyExpenseSummary.java
│   │   │   │   ├── MonthlyIncomeSummary.java
│   │   │   │   ├── Savings.java
│   │   │   │   └── User.java
│   │   │   ├── repository/                 # Data Access Layer
│   │   │   │   ├── ExpenseRepository.java
│   │   │   │   ├── IncomeRepository.java
│   │   │   │   ├── SavingsRepository.java
│   │   │   │   └── UserRepository.java
│   │   │   └── service/                    # Business Logic Layer
│   │   │       ├── AnalyticsService.java
│   │   │       ├── AuthService.java
│   │   │       ├── BudgetService.java
│   │   │       ├── ExpenseService.java
│   │   │       ├── IncomeService.java
│   │   │       ├── SavingsService.java
│   │   │       └── UserService.java
│   │   └── resources/
│   │       ├── application.properties      # Central application configuration
│   │       ├── static/                     # Static CSS, JS & Image assets
│   │       │   └── css/index.css
│   │       └── templates/                  # Thymeleaf HTML Templates
│   │           ├── budget.html
│   │           ├── dashboard.html
│   │           ├── edit_expense.html
│   │           ├── edit_income.html
│   │           ├── expense.html
│   │           ├── expense_evaluation.html
│   │           ├── income.html
│   │           ├── index.html
│   │           ├── login.html
│   │           ├── register.html
│   │           └── settings.html
│   └── test/                               # Unit & Integration Tests
│       └── java/com/smartexpensetracker/
│           └── SmartExpenseTrackerApplicationTests.java
├── .gitignore                              # Comprehensive Git ignore rules
├── pom.xml                                 # Maven dependencies & build configuration
└── README.md                               # Project documentation
```

---

## 🛠️ Tech Stack

| Component | Technology | Description |
|---|---|---|
| **Backend Framework** | Spring Boot 4.0 / 3.x | Web MVC, Dependency Injection |
| **Language** | Java 21 LTS | Modern Java syntax & features |
| **Database** | MySQL 8.0+ / MariaDB | Relational storage & indexing |
| **Data Access** | Spring JDBC (`JdbcTemplate`) | Type-safe, parameterized queries |
| **Security** | Spring Security Crypto | BCrypt password hashing |
| **Template Engine** | Thymeleaf 3 | Server-side HTML rendering |
| **Data Visualization** | Chart.js | Interactive pie & bar charts |
| **Build Tool** | Apache Maven 3.9+ | Dependency & build lifecycle |

---

## ⚙️ Prerequisites

Before running the application, make sure you have installed:

- **JDK 21 or higher** (`java -version`)
- **Apache Maven 3.9+** (`mvn -version`)
- **MySQL Server 8.0+** (or XAMPP / MariaDB)

---

## 🗄️ Database Setup

1. Open your MySQL terminal or phpMyAdmin.
2. Execute the schema script located at `database/schema.sql`:

```sql
SOURCE database/schema.sql;
```

3. *(Optional)* If you want demo test data with a pre-configured user (`demo@example.com` / `password123`):

```sql
SOURCE database/seed_data.sql;
```

---

## 🔧 Configuration

Update your database credentials in `src/main/resources/application.properties` or set environment variables:

```properties
spring.datasource.url=jdbc:mysql://${DB_HOST:localhost}:${DB_PORT:3306}/${DB_NAME:expense_tracker}?useSSL=false&serverTimezone=Asia/Kolkata&allowPublicKeyRetrieval=true&characterEncoding=UTF-8
spring.datasource.username=${DB_USER:root}
spring.datasource.password=${DB_PASSWORD:NewPassword@123}
```

| Environment Variable | Default Value | Description |
|---|---|---|
| `PORT` | `8080` | Web server port |
| `DB_HOST` | `localhost` | MySQL host address |
| `DB_PORT` | `3306` | MySQL port |
| `DB_NAME` | `expense_tracker` | Database name |
| `DB_USER` | `root` | Database username |
| `DB_PASSWORD` | `NewPassword@123` | Database password |

---

## 🚀 Build & Run

### 1. Compile and Package
```bash
mvn clean compile
```

### 2. Run Application
```bash
mvn spring-boot:run
```

### 3. Open in Browser
Visit [http://localhost:8080](http://localhost:8080) to access the Smart Expense Tracker.

---

## 🗺️ Application Endpoints

| Endpoint | Method | Description |
|---|---|---|
| `/` | `GET` | Landing page |
| `/register` | `GET`, `POST` | User registration |
| `/login` | `GET`, `POST` | User login |
| `/logout` | `GET` | User session logout |
| `/dashboard` | `GET` | Main overview dashboard |
| `/income` | `GET`, `POST` | Income history & add income |
| `/edit_income/{id}` | `GET`, `POST` | Edit existing income record |
| `/delete_income/{id}`| `GET` | Delete income record |
| `/save_savings` | `POST` | Set monthly savings target |
| `/expense` | `GET`, `POST` | Expense history & add expense |
| `/edit_expense/{id}`| `GET`, `POST` | Edit existing expense record |
| `/delete_expense/{id}`| `GET`| Delete expense record |
| `/expense_evaluation`| `GET` | Charts & category breakdown |
| `/budget` | `GET` | Budget health & alert status |
| `/settings` | `GET` | Profile & security settings |
| `/settings/profile` | `POST`| Update profile name & email |
| `/settings/password`| `POST`| Change account password |

---

## 📄 License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.