package com.smartexpensetracker.controller;

import com.smartexpensetracker.dto.IncomeRequest;
import com.smartexpensetracker.model.Income;
import com.smartexpensetracker.model.MonthlyIncomeSummary;
import com.smartexpensetracker.service.IncomeService;
import com.smartexpensetracker.service.SavingsService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller managing income records, history, monthly earnings, and savings allocations.
 */
@Controller
public class IncomeController {

    private final IncomeService incomeService;
    private final SavingsService savingsService;

    public IncomeController(IncomeService incomeService, SavingsService savingsService) {
        this.incomeService = incomeService;
        this.savingsService = savingsService;
    }

    private Integer getAuthenticatedUserId(HttpSession session) {
        Object uid = session.getAttribute("user_id");
        return uid != null ? ((Number) uid).intValue() : null;
    }

    @RequestMapping(value = "/income", method = {RequestMethod.GET, RequestMethod.POST})
    public String income(@RequestParam(required = false) Double amount,
                         @RequestParam(required = false) String date,
                         @RequestParam(required = false) String description,
                         HttpSession session,
                         Model model) {
        Integer userId = getAuthenticatedUserId(session);
        if (userId == null) {
            return "redirect:/login";
        }

        if (amount != null && date != null && !date.isBlank()) {
            IncomeRequest req = new IncomeRequest(amount, LocalDate.parse(date), description);
            incomeService.addIncome(userId, req);
        }

        List<Income> incomeList = incomeService.getIncomeHistory(userId);
        List<MonthlyIncomeSummary> monthlyList = incomeService.getMonthlyIncomeSummaries(userId);

        String latestMonth = monthlyList.isEmpty() ? null : monthlyList.get(0).getMonth();
        Double latestTotal = monthlyList.isEmpty() ? 0.0 : monthlyList.get(0).getTotalIncome();
        double savingsAmount = latestMonth != null ? savingsService.getSavingsForMonth(userId, latestMonth) : 0.0;

        model.addAttribute("income", incomeList);
        model.addAttribute("monthlyIncome", monthlyList);
        model.addAttribute("latestMonth", latestMonth);
        model.addAttribute("latestTotal", latestTotal);
        model.addAttribute("savingsAmount", savingsAmount);

        return "income";
    }

    @RequestMapping(value = "/edit_income/{id}", method = {RequestMethod.GET, RequestMethod.POST})
    public String editIncome(@PathVariable int id,
                             @RequestParam(required = false) Double amount,
                             @RequestParam(required = false) String date,
                             @RequestParam(required = false) String description,
                             HttpSession session,
                             Model model) {
        Integer userId = getAuthenticatedUserId(session);
        if (userId == null) {
            return "redirect:/login";
        }

        if (amount != null && date != null && !date.isBlank()) {
            IncomeRequest req = new IncomeRequest(amount, LocalDate.parse(date), description);
            incomeService.updateIncome(id, req);
            return "redirect:/income";
        }

        Income income = incomeService.getIncomeById(id);
        model.addAttribute("data", income);
        return "edit_income";
    }

    @GetMapping("/delete_income/{id}")
    public String deleteIncome(@PathVariable int id, HttpSession session) {
        if (getAuthenticatedUserId(session) == null) {
            return "redirect:/login";
        }
        incomeService.deleteIncome(id);
        return "redirect:/income";
    }

    @PostMapping("/save_savings")
    public String saveSavings(@RequestParam String month,
                              @RequestParam Double savings,
                              HttpSession session) {
        Integer userId = getAuthenticatedUserId(session);
        if (userId == null) {
            return "redirect:/login";
        }
        savingsService.saveSavings(userId, month, savings);
        return "redirect:/income";
    }
}
