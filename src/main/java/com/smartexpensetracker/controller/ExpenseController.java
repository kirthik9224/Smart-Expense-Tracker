package com.smartexpensetracker.controller;

import com.smartexpensetracker.dto.ExpenseRequest;
import com.smartexpensetracker.model.Expense;
import com.smartexpensetracker.model.MonthlyExpenseSummary;
import com.smartexpensetracker.service.ExpenseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller managing expense tracking, history, monthly summaries, and updates.
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
            ExpenseRequest req = new ExpenseRequest(amount, LocalDate.parse(date), category, description);
            String message = expenseService.addExpense(userId, req);
            model.addAttribute("message", message);
        }

        List<Expense> expenseList = expenseService.getExpenseHistory(userId);
        List<MonthlyExpenseSummary> monthlyList = expenseService.getMonthlyExpenses(userId);

        model.addAttribute("expenses", expenseList);
        model.addAttribute("monthlyExpense", monthlyList);

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
            ExpenseRequest req = new ExpenseRequest(amount, LocalDate.parse(date), category, description);
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
}
