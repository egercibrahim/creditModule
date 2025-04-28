package com.egercibrahim.creditModule.record;

import java.math.BigDecimal;

public record CreateLoanRequest(Long customerId,
                                BigDecimal amount,
                                BigDecimal interestRate,
                                Integer numberOfInstallments) {
}