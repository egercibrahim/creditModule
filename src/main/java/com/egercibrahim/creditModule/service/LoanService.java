package com.egercibrahim.creditModule.service;

import com.egercibrahim.creditModule.enums.Role;
import com.egercibrahim.creditModule.model.Customer;
import com.egercibrahim.creditModule.model.Loan;
import com.egercibrahim.creditModule.model.LoanInstallment;
import com.egercibrahim.creditModule.model.User;
import com.egercibrahim.creditModule.repository.LoanInstallmentRepository;
import com.egercibrahim.creditModule.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final UserService userService;
    private final CustomerService customerService;
    private final LoanRepository loanRepository;
    private final LoanInstallmentRepository installmentRepository;

    private final List<Integer> allowedInstallments = List.of(6, 9, 12, 24);

    public Loan getLoanById(String userName,
                            Long loanId) {
        User user = userService.findByUsername(userName);
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        if (user.getRole().equals(Role.CUSTOMER)) {
            Customer customer = customerService.findByUser(user);
            if (!customer.getId().equals(loan.getCustomer().getId())) {
                throw new RuntimeException("User must have ADMIN role to access the loan");
            }
            return loan;
        } else if (user.getRole().equals(Role.ADMIN)) {
            return loan;
        } else {
            throw new RuntimeException("Unauthorized");
        }
    }

    public List<Loan> getLoansByCustomer(Customer customer) {
        return loanRepository.findByCustomer(customer);
    }

    public List<Loan> getLoansByCustomer(String userName,
                                         Long customerId,
                                         Integer numberOfInstallments,
                                         Boolean isPaid) {
        User user = userService.findByUsername(userName);
        Customer customer = null;
        if (user.getRole().equals(Role.CUSTOMER)) {
            customer = customerService.findByUser(user);
        } else if (user.getRole().equals(Role.ADMIN)) {
            if (customerId == null) {
                throw new RuntimeException("CustomerId can not be null for ADMIN role");
            }
            customer = customerService.getCustomerById(customerId);
        } else {
            throw new RuntimeException("Unauthorized");
        }
        return getLoansByCustomer(customer).stream()
                .filter(e -> isPaid == null || e.getIsPaid().equals(isPaid))
                .filter(e -> numberOfInstallments == null || e.getNumberOfInstallments().equals(numberOfInstallments))
                .toList();
    }

    public Loan save(Loan loan) {
        return loanRepository.save(loan);
    }

    @Transactional
    public Loan createLoan(Long customerId, BigDecimal amount, BigDecimal interestRate, Integer numberOfInstallments) {
        if (!allowedInstallments.contains(numberOfInstallments)) {
            throw new RuntimeException("Installments can only be 6, 9, 12, 24");
        }
        if (interestRate.compareTo(BigDecimal.valueOf(0.1)) < 0 || interestRate.compareTo(BigDecimal.valueOf(0.5)) > 0) {
            throw new RuntimeException("Interest rate must be between 0.1 and 0.5");
        }

        Customer customer = customerService.getCustomerById(customerId);

        BigDecimal totalLoanAmount = amount.multiply(BigDecimal.ONE.add(interestRate));
        if (customer.getCreditLimit().subtract(customer.getUsedCreditLimit()).compareTo(totalLoanAmount) < 0) {
            throw new RuntimeException("Customer does not have enough credit limit");
        }

        Loan loan = Loan.builder()
                .customer(customer)
                .loanAmount(totalLoanAmount)
                .numberOfInstallments(numberOfInstallments)
                .createDate(LocalDate.now())
                .isPaid(false)
                .build();

        loan = loanRepository.save(loan);

        BigDecimal installmentAmount = totalLoanAmount.divide(BigDecimal.valueOf(numberOfInstallments), RoundingMode.HALF_UP);

        List<LoanInstallment> installments = new ArrayList<>();
        LocalDate dueDate = LocalDate.now().plusMonths(1).withDayOfMonth(1);

        for (int i = 0; i < numberOfInstallments; i++) {
            installments.add(
                    LoanInstallment.builder()
                            .loan(loan)
                            .amount(installmentAmount)
                            .paidAmount(BigDecimal.ZERO)
                            .dueDate(dueDate)
                            .isPaid(false)
                            .build()
            );
            dueDate = dueDate.plusMonths(1);
        }

        installmentRepository.saveAll(installments);

        customer.setUsedCreditLimit(customer.getUsedCreditLimit().add(totalLoanAmount));
        customerService.save(customer);

        return loan;
    }
}
