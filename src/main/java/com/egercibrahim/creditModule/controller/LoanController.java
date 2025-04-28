package com.egercibrahim.creditModule.controller;

import com.egercibrahim.creditModule.model.Loan;
import com.egercibrahim.creditModule.record.CreateLoanRequest;
import com.egercibrahim.creditModule.record.LoanRecord;
import com.egercibrahim.creditModule.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/credit/loan")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @GetMapping
    public List<LoanRecord> listLoans(Authentication authentication,
                                      @RequestParam(required = false) Long customerId,
                                      @RequestParam(required = false) Integer numberOfInstallments,
                                      @RequestParam(required = false) Boolean isPaid) {
        return loanService.getLoansByCustomer(authentication.getName(), customerId, numberOfInstallments, isPaid)
                .stream().map(Loan::toRecord).toList();
    }

    @PostMapping
    public LoanRecord createLoan(@RequestBody CreateLoanRequest request) {
        return loanService.createLoan(
                        request.customerId(),
                        request.amount(),
                        request.interestRate(),
                        request.numberOfInstallments())
                .toRecord();
    }
}
