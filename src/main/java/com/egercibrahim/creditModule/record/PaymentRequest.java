package com.egercibrahim.creditModule.record;

import java.math.BigDecimal;

public record PaymentRequest(Long loanId,
                             BigDecimal amount) {
}
