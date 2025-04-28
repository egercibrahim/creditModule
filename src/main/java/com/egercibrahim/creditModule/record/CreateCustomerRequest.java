package com.egercibrahim.creditModule.record;

import java.math.BigDecimal;

public record CreateCustomerRequest(String username,
                                    String name,
                                    String surname,
                                    BigDecimal creditLimit) {
}