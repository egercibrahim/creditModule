package com.egercibrahim.creditModule.repository;

import com.egercibrahim.creditModule.model.Loan;
import com.egercibrahim.creditModule.model.LoanInstallment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanInstallmentRepository extends JpaRepository<LoanInstallment, Long> {
    List<LoanInstallment> findByLoanOrderByDueDateAsc(Loan loan);
}
