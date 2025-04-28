package com.egercibrahim.creditModule.repository;

import com.egercibrahim.creditModule.model.Customer;
import com.egercibrahim.creditModule.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByCustomer(Customer customer);
}
