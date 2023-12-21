package com.example.HIkeOn;

import com.example.HIkeOn.model.Customer;
import com.example.HIkeOn.model.Occupation;
import com.example.HIkeOn.model.CustomerGroup;
import com.example.HIkeOn.repository.CustomerRepository;
import com.example.HIkeOn.services.CustomerService;
import com.example.HIkeOn.services.CustomerServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
// import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService = new CustomerServiceImpl();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveCustomer_ValidCustomer_Success() {
        // Arrange
        Customer customer = new Customer("John Doe", "john.doe@example.com", LocalDate.of(1990, 1, 1), Occupation.DEVELOPER);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerRepository.existsByEmail(anyString())).thenReturn(false);
        when(customerRepository.existsByOccupationAndDobAndCustomerGroup(any(), any(), any())).thenReturn(false);

        // Act
        Customer savedCustomer = customerService.saveCustomer(customer);

        // Assert
        assertNotNull(savedCustomer);
        assertEquals("John Doe", savedCustomer.getName());
        assertEquals("john.doe@example.com", savedCustomer.getEmail());
        assertEquals(LocalDate.of(1990, 1, 1), savedCustomer.getDob());
        assertEquals(Occupation.DEVELOPER, savedCustomer.getOccupation());
        assertEquals(CustomerGroup.DEVELOPER, savedCustomer.getCustomerGroup());

        // Verify that save method was called once
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void saveCustomer_DuplicateEmail_ExceptionThrown() {
        // Arrange
        Customer customer = new Customer("John Doe", "john.doe@example.com", LocalDate.of(1990, 1, 1), Occupation.DEVELOPER);
        when(customerRepository.existsByEmail(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> customerService.saveCustomer(customer));

        // Verify that save method was not called
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void saveCustomer_Under18_ExceptionThrown() {
        // Arrange
        Customer customer = new Customer("John Doe", "john.doe@example.com", LocalDate.now().minusYears(17), Occupation.DEVELOPER);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> customerService.saveCustomer(customer));

        // Verify that save method was not called
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void getAllCustomers_ReturnsListOfCustomers() {
        // Arrange
        when(customerRepository.findAll()).thenReturn(Collections.singletonList(
                new Customer("John Doe", "john.doe@example.com", LocalDate.of(1990, 1, 1), Occupation.DEVELOPER)
        ));

        // Act
        List<Customer> customers = customerService.getAllCustomers();

        // Assert
        assertNotNull(customers);
        assertEquals(1, customers.size());

        // Verify that findAll method was called once
        verify(customerRepository, times(1)).findAll();
    }
}
