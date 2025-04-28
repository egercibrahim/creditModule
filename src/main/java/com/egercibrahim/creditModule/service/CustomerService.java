package com.egercibrahim.creditModule.service;

import com.egercibrahim.creditModule.model.Customer;
import com.egercibrahim.creditModule.model.User;
import com.egercibrahim.creditModule.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final UserService userService;

    public Customer getCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    public Customer findByUser(User user) {
        return customerRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Customer save(String username, String name, String surname, BigDecimal creditLimit) {
        User user = userService.findByUsername(username);
        Customer customer = Customer.builder()
                .user(user)
                .name(name)
                .surname(surname)
                .creditLimit(creditLimit)
                .usedCreditLimit(BigDecimal.ZERO)
                .build();
        return save(customer);
    }

    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }
}
