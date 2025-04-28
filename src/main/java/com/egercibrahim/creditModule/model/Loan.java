package com.egercibrahim.creditModule.model;

import com.egercibrahim.creditModule.record.LoanRecord;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Loan {

    @Id
    @SequenceGenerator(name = "load_seq_id", sequenceName = "load_seq_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "load_seq_id")
    private Long id;
    @ManyToOne
    private Customer customer;
    private BigDecimal loanAmount;
    private Integer numberOfInstallments;
    private LocalDate createDate;
    private Boolean isPaid;

    public LoanRecord toRecord() {
        return new LoanRecord(
                this.id,
                this.customer.getId(),
                this.loanAmount,
                this.numberOfInstallments,
                this.createDate,
                this.isPaid);
    }
}
