package com.example.HIkeOn.repository;

import com.example.HIkeOn.model.Customer;
import com.example.HIkeOn.model.Occupation;
import com.example.HIkeOn.model.CustomerGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByOccupationAndDobAndCustomerGroup(Occupation occupation, LocalDate dob, CustomerGroup customerGroup);
    Optional<Customer> findByEmail(String email);
    boolean existsByEmail(String email);
    // Other query methods as needed
}
