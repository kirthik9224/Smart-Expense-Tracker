package com.smartexpensetracker.service;

import com.smartexpensetracker.dto.IncomeRequest;
import com.smartexpensetracker.exception.ResourceNotFoundException;
import com.smartexpensetracker.model.Income;
import com.smartexpensetracker.model.MonthlyIncomeSummary;
import com.smartexpensetracker.repository.IncomeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service managing income operations and monthly earnings tracking.
 */
@Service
public class IncomeService {

    private final IncomeRepository incomeRepository;

    public IncomeService(IncomeRepository incomeRepository) {
        this.incomeRepository = incomeRepository;
    }

    public void addIncome(Integer userId, IncomeRequest request) {
        incomeRepository.save(userId, request.getAmount(), request.getDate(), request.getDescription());
    }

    public List<Income> getIncomeHistory(Integer userId) {
        return incomeRepository.findByUserIdOrderByDateDesc(userId);
    }

    public Income getIncomeById(Integer incomeId) {
        return incomeRepository.findById(incomeId)
                .orElseThrow(() -> new ResourceNotFoundException("Income record not found with ID: " + incomeId));
    }

    public void updateIncome(Integer incomeId, IncomeRequest request) {
        incomeRepository.update(incomeId, request.getAmount(), request.getDate(), request.getDescription());
    }

    public void deleteIncome(Integer incomeId) {
        incomeRepository.deleteById(incomeId);
    }

    public List<MonthlyIncomeSummary> getMonthlyIncomeSummaries(Integer userId) {
        return incomeRepository.findMonthlyIncomeWithSavings(userId);
    }

    public double getTotalIncomeForMonth(Integer userId, String monthYear) {
        return incomeRepository.getTotalIncomeForMonth(userId, monthYear);
    }
}
