package com.egercibrahim.creditModule.record;

import java.math.BigDecimal;

public record PaymentResult(Integer numberOfInstallmentsPaid,
                            BigDecimal totalAmountSpent,
                            Boolean isLoanFullyPaid) {
}
