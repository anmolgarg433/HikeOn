package com.example.HIkeOn.services;

import com.example.HIkeOn.model.Customer;

import java.util.List;

public interface CustomerService {
    Customer saveCustomer(Customer customer);
    List<Customer> getAllCustomers();
    // Other methods as needed
}
