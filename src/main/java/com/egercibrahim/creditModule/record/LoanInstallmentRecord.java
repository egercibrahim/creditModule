package com.egercibrahim.creditModule.record;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LoanInstallmentRecord(Long id,
                                    Long loanId,
                                    BigDecimal amount,
                                    BigDecimal paidAmount,
                                    LocalDate dueDate,
                                    LocalDate paymentDate,
                                    Boolean isPaid) {
}
