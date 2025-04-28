package com.egercibrahim.creditModule.repository;

import com.egercibrahim.creditModule.model.Customer;
import com.egercibrahim.creditModule.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByUser(User user);
}
