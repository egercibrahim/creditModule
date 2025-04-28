package com.egercibrahim.creditModule.model;

import com.egercibrahim.creditModule.record.LoanInstallmentRecord;
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
public class LoanInstallment {

    @Id
    @SequenceGenerator(name = "loan_installment_seq_id", sequenceName = "loan_installment_seq_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "loan_installment_seq_id")
    private Long id;
    @ManyToOne
    private Loan loan;
    private BigDecimal amount;
    private BigDecimal paidAmount;
    private LocalDate dueDate;
    private LocalDate paymentDate;
    private Boolean isPaid;

    public LoanInstallmentRecord toRecord() {
        return new LoanInstallmentRecord(
                this.id,
                this.loan.getId(),
                this.amount,
                this.paidAmount,
                this.dueDate,
                this.paymentDate,
                this.isPaid);
    }
}
