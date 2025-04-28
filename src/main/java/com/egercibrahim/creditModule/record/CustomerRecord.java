package com.egercibrahim.creditModule.record;

import java.math.BigDecimal;

public record CustomerRecord(Long id,
                             String name,
                             String surname,
                             BigDecimal creditLimit,
                             BigDecimal usedCreditLimit) {
}
