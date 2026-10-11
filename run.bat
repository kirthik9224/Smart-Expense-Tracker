@echo off
rem Organization: Orbytes Global Solutions, Owner: Logaraj S, Created at: 2026-10-11
rem Description: Convenience launcher script for Smart Expense Tracker application

set PORT=8081
echo Starting Smart Expense Tracker on http://localhost:%PORT% ...
java -jar target\smart-expense-tracker-1.0.0.jar --server.port=%PORT%
pause
