package com.example.HIkeOn.services;

import com.example.HIkeOn.model.Customer;
import com.example.HIkeOn.model.Occupation;
import com.example.HIkeOn.model.CustomerGroup;
import com.example.HIkeOn.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.time.Period;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Customer saveCustomer(Customer customer) {
        // Check if the user's DOB indicates they are below 18 years old
        if (isBelow18(customer.getDob())) {
            throw new IllegalArgumentException("Customer must be 18 years or older.");
        }

        // Assign customer group based on rules
        if (customer.getEmail().endsWith("@hikeon.tech")) {
            customer.setCustomerGroup(CustomerGroup.HIKEON);
        } else if (customer.getOccupation() == Occupation.DEVELOPER) {
            customer.setCustomerGroup(CustomerGroup.DEVELOPER);
        } else if (customer.getOccupation() == Occupation.CHEF) {
            customer.setCustomerGroup(CustomerGroup.CHEF);
        } else {
            // For any other domain and occupations, assign the customer group as 'NA'
            customer.setCustomerGroup(CustomerGroup.NA);
        }

        // Check for uniqueness constraints
        validateUniqueness(customer);

        // Save to the database
        return customerRepository.save(customer);
    }

    private boolean isBelow18(LocalDate dob) {
        LocalDate currentDate = LocalDate.now();
        return Period.between(dob, currentDate).getYears() < 18;
    }

    private void validateUniqueness(Customer customer) {
        // Check if email is unique
        if (customerRepository.findByEmail(customer.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email address must be unique.");
        }

        // Check if the combination of occupation, DOB, and customer group is unique
        if (customerRepository.existsByOccupationAndDobAndCustomerGroup(
                customer.getOccupation(), customer.getDob(), customer.getCustomerGroup())) {
            throw new IllegalArgumentException("Customer with the same occupation, DOB, and group already exists.");
        }
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
    // Other methods as needed
}

