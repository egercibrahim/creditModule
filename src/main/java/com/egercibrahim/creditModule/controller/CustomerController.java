package com.egercibrahim.creditModule.controller;

import com.egercibrahim.creditModule.model.Customer;
import com.egercibrahim.creditModule.record.CreateCustomerRequest;
import com.egercibrahim.creditModule.record.CustomerRecord;
import com.egercibrahim.creditModule.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/credit/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<CustomerRecord> findAll() {
        return customerService.findAll()
                .stream().map(Customer::toRecord).toList();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public CustomerRecord createCustomer(@RequestBody CreateCustomerRequest request) {
        return customerService.save(
                        request.username(),
                        request.name(),
                        request.surname(),
                        request.creditLimit())
                .toRecord();
    }
}
