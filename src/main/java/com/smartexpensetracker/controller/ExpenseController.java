/*
 * Organization: Orbytes Global Solutions, Owner: Logaraj S, Created at: 2026-10-11
 * Description: Controller managing expense tracking, history, monthly summaries, and CSV export.
 */
package com.smartexpensetracker.controller;

import com.smartexpensetracker.dto.ExpenseRequest;
import com.smartexpensetracker.model.Expense;
import com.smartexpensetracker.model.MonthlyExpenseSummary;
import com.smartexpensetracker.service.ExpenseService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

/**
 * Controller managing expense tracking, history, monthly summaries, updates, and CSV exports.
 */
@Controller
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    private Integer getAuthenticatedUserId(HttpSession session) {
        Object uid = session.getAttribute("user_id");
        return uid != null ? ((Number) uid).intValue() : null;
    }

    @RequestMapping(value = "/expense", method = {RequestMethod.GET, RequestMethod.POST})
    public String expense(@RequestParam(required = false) Double amount,
                          @RequestParam(required = false) String date,
                          @RequestParam(required = false) String category,
                          @RequestParam(required = false) String description,
                          HttpSession session,
                          Model model) {
        Integer userId = getAuthenticatedUserId(session);
        if (userId == null) {
            return "redirect:/login";
        }

        if (amount != null && date != null && !date.isBlank()) {
            ExpenseRequest req = new ExpenseRequest(
                    amount,
                    LocalDate.parse(date),
                    category,
                    description
            );
            String message = expenseService.addExpense(userId, req);
            model.addAttribute("message", message);
        }

        List<Expense> expenseList = expenseService.getExpenseHistory(userId);
        List<MonthlyExpenseSummary> monthlyList = expenseService.getMonthlyExpenses(userId);

        double totalAllTime = expenseList.stream().mapToDouble(Expense::getAmount).sum();

        model.addAttribute("expenses", expenseList);
        model.addAttribute("monthlyExpense", monthlyList);
        model.addAttribute("totalAllTime", totalAllTime);

        return "expense";
    }

    @RequestMapping(value = "/edit_expense/{id}", method = {RequestMethod.GET, RequestMethod.POST})
    public String editExpense(@PathVariable int id,
                              @RequestParam(required = false) Double amount,
                              @RequestParam(required = false) String date,
                              @RequestParam(required = false) String category,
                              @RequestParam(required = false) String description,
                              HttpSession session,
                              Model model) {
        Integer userId = getAuthenticatedUserId(session);
        if (userId == null) {
            return "redirect:/login";
        }

        if (amount != null && date != null && !date.isBlank()) {
            ExpenseRequest req = new ExpenseRequest(
                    amount,
                    LocalDate.parse(date),
                    category,
                    description
            );
            expenseService.updateExpense(id, req);
            return "redirect:/expense";
        }

        Expense expense = expenseService.getExpenseById(id);
        model.addAttribute("data", expense);
        return "edit_expense";
    }

    @GetMapping("/delete_expense/{id}")
    public String deleteExpense(@PathVariable int id, HttpSession session) {
        if (getAuthenticatedUserId(session) == null) {
            return "redirect:/login";
        }
        expenseService.deleteExpense(id);
        return "redirect:/expense";
    }

    @GetMapping("/expense/export")
    public void exportToCsv(HttpSession session, HttpServletResponse response) throws IOException {
        Integer userId = getAuthenticatedUserId(session);
        if (userId == null) {
            response.sendRedirect("/login");
            return;
        }

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"expenses.csv\"");

        List<Expense> list = expenseService.getExpenseHistory(userId);
        PrintWriter writer = response.getWriter();
        writer.println("Expense ID,Category,Amount,Date,Description");

        for (Expense exp : list) {
            String safeDesc = exp.getDescription() != null
                    ? exp.getDescription().replace("\"", "\"\"")
                    : "";
            String safeCat = exp.getCategory() != null
                    ? exp.getCategory().replace("\"", "\"\"")
                    : "";
            writer.printf("%d,\"%s\",%.2f,%s,\"%s\"%n",
                    exp.getExpenseId(),
                    safeCat,
                    exp.getAmount(),
                    exp.getExpenseDate(),
                    safeDesc);
        }
        writer.flush();
    }
}
