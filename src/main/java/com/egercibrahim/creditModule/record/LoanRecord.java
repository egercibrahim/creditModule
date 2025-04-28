package com.egercibrahim.creditModule.record;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LoanRecord(Long id,
                         Long customerId,
                         BigDecimal loanAmount,
                         Integer numberOfInstallments,
                         LocalDate createDate,
                         Boolean isPaid) {
}
