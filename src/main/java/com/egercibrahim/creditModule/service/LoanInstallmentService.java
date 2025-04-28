package com.egercibrahim.creditModule.service;

import com.egercibrahim.creditModule.model.Loan;
import com.egercibrahim.creditModule.model.LoanInstallment;
import com.egercibrahim.creditModule.record.PaymentResult;
import com.egercibrahim.creditModule.repository.LoanInstallmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanInstallmentService {

    private final LoanService loanService;
    private final LoanInstallmentRepository installmentRepository;

    public List<LoanInstallment> getInstallmentsByLoan(String userName, Long loanId) {
        Loan loan = loanService.getLoanById(userName, loanId);
        return installmentRepository.findByLoanOrderByDueDateAsc(loan);
    }

    @Transactional
    public PaymentResult payLoan(String userName, Long loanId, BigDecimal amount) {
        Loan loan = loanService.getLoanById(userName, loanId);

        List<LoanInstallment> installments = installmentRepository.findByLoanOrderByDueDateAsc(loan);

        LocalDate today = LocalDate.now();
        BigDecimal totalSpent = BigDecimal.ZERO;
        int paidCount = 0;

        for (LoanInstallment installment : installments) {
            if (installment.getIsPaid()) {
                continue; // Already paid
            }

            // Check if due date is within next 3 months
            if (ChronoUnit.MONTHS.between(today.withDayOfMonth(1), installment.getDueDate().withDayOfMonth(1)) > 2) {
                continue;
            }

            BigDecimal installmentAmount = installment.getAmount();

            // Reward or Penalty Logic
            long daysDifference = ChronoUnit.DAYS.between(today, installment.getDueDate());
            if (daysDifference > 0) { // Early Payment => Discount
                BigDecimal discount = installmentAmount.multiply(BigDecimal.valueOf(0.001 * daysDifference));
                installmentAmount = installmentAmount.subtract(discount);
            } else if (daysDifference < 0) { // Late Payment => Penalty
                BigDecimal penalty = installmentAmount.multiply(BigDecimal.valueOf(0.001 * Math.abs(daysDifference)));
                installmentAmount = installmentAmount.add(penalty);
            }

            // Check if enough amount is left
            if (amount.compareTo(installmentAmount) >= 0) {
                installment.setPaidAmount(installmentAmount);
                installment.setPaymentDate(today);
                installment.setIsPaid(true);
                installmentRepository.save(installment);

                amount = amount.subtract(installmentAmount);
                totalSpent = totalSpent.add(installmentAmount);
                paidCount++;
            } else {
                break; // Cannot pay partially
            }
        }

        // If all installments paid => Mark Loan Paid
        boolean allPaid = installments.stream().allMatch(LoanInstallment::getIsPaid);
        if (allPaid) {
            loan.setIsPaid(true);
            loanService.save(loan);
        }

        return new PaymentResult(paidCount, totalSpent, loan.getIsPaid());
    }
}
