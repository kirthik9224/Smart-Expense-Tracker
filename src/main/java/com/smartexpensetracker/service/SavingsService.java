package com.smartexpensetracker.service;

import com.smartexpensetracker.repository.SavingsRepository;
import org.springframework.stereotype.Service;

/**
 * Service managing monthly savings goals and allocations.
 */
@Service
public class SavingsService {

    private final SavingsRepository savingsRepository;

    public SavingsService(SavingsRepository savingsRepository) {
        this.savingsRepository = savingsRepository;
    }

    public void saveSavings(Integer userId, String monthYear, Double amount) {
        savingsRepository.saveOrUpdate(userId, monthYear, amount);
    }

    public double getSavingsForMonth(Integer userId, String monthYear) {
        return savingsRepository.getSavingsForMonth(userId, monthYear);
    }
}
