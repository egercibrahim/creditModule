package com.egercibrahim.creditModule.model;

import com.egercibrahim.creditModule.record.CustomerRecord;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@Entity
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @SequenceGenerator(name = "customer_seq_id", sequenceName = "customer_seq_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "customer_seq_id")
    private Long id;
    @OneToOne
    private User user;
    private String name;
    private String surname;
    private BigDecimal creditLimit;
    private BigDecimal usedCreditLimit;

    public CustomerRecord toRecord() {
        return new CustomerRecord(
                this.id,
                this.name,
                this.surname,
                this.creditLimit,
                this.usedCreditLimit);
    }
}
