package com.egercibrahim.creditModule.controller;

import com.egercibrahim.creditModule.model.LoanInstallment;
import com.egercibrahim.creditModule.record.LoanInstallmentRecord;
import com.egercibrahim.creditModule.record.PaymentRequest;
import com.egercibrahim.creditModule.record.PaymentResult;
import com.egercibrahim.creditModule.service.LoanInstallmentService;
import com.egercibrahim.creditModule.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/credit/installment")
@RequiredArgsConstructor
public class LoanInstallmentController {

    private final LoanService loanService;
    private final LoanInstallmentService loanInstallmentService;

    @GetMapping
    public List<LoanInstallmentRecord> getInstallments(Authentication authentication,
                                                       @RequestParam Long loanId) {
        return loanInstallmentService.getInstallmentsByLoan(authentication.getName(), loanId)
                .stream().map(LoanInstallment::toRecord).toList();
    }

    @PostMapping
    public PaymentResult payLoan(Authentication authentication,
                                 @RequestBody PaymentRequest request) {
        return loanInstallmentService.payLoan(authentication.getName(), request.loanId(), request.amount());
    }
}
