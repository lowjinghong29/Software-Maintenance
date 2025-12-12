package oop_assignment.service.impl;

import oop_assignment.exception.DuplicateCustomerException;
import oop_assignment.exception.InsufficientBalanceException;
import oop_assignment.exception.InvalidInputException;
import oop_assignment.model.Customer;
import oop_assignment.repository.CustomerRepository;
import oop_assignment.service.CustomerAccountService;
import oop_assignment.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class CustomerAccountServiceImplTest {

    private CustomerAccountService customerAccountService;
    private CustomerService customerService;
    private InMemoryCustomerRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryCustomerRepository();
        customerAccountService = new CustomerAccountServiceImpl(repository);
        customerService = new CustomerServiceImpl(repository);
    }

    @Test
    void testHasEnoughBalance_TrueAndFalse() {
        Customer customer = new Customer("C010","John", "pass", "john@example.com", "123", "Addr", 100, 100.00);
        repository.addCustomer(customer);

        assertTrue(customerAccountService.hasEnoughBalance(customer, 50.00));
        assertFalse(customerAccountService.hasEnoughBalance(customer, 150.00));
    }

    @Test
    void testDebitBalance_SufficientBalance_UpdatesAndPersists() {
        Customer customer = new Customer("C010","John", "pass", "john@example.com", "123", "Addr", 100, 100.00);
        repository.addCustomer(customer);

        customerAccountService.debitBalance(customer, 40.00);

        assertEquals(60.00, customer.getBalance(), 0.01);
        Customer persisted = repository.findByEmail("john@example.com");
        assertEquals(60.00, persisted.getBalance(), 0.01);
    }

    @Test
    void testDebitBalance_Insufficient_ThrowsInsufficientBalanceException() {
        Customer customer = new Customer("C010","John", "pass", "john@example.com", "123", "Addr", 100, 50.00);
        repository.addCustomer(customer);

        assertThrows(InsufficientBalanceException.class, () -> customerAccountService.debitBalance(customer, 100.00));
    }

    @Test
    void testAddBalance_PositiveAmount_IncreasesBalanceAndPersists() {
        Customer customer = new Customer("C010","John", "pass", "john@example.com", "123", "Addr", 100, 50.00);
        repository.addCustomer(customer);

        customerAccountService.addBalance(customer, 25.00);

        assertEquals(75.00, customer.getBalance(), 0.01);
        Customer persisted = repository.findByEmail("john@example.com");
        assertEquals(75.00, persisted.getBalance(), 0.01);
    }

    @Test
    void testAddBalance_NonPositiveAmount_ThrowsInvalidInputException() {
        Customer customer = new Customer("C010","John", "pass", "john@example.com", "123", "Addr", 100, 50.00);
        repository.addCustomer(customer);

        assertThrows(InvalidInputException.class, () -> customerAccountService.addBalance(customer, 0));
        assertThrows(InvalidInputException.class, () -> customerAccountService.addBalance(customer, -10));
    }

    @Test
    void testAddPoints_AddsPointsAndPersists() {
        Customer customer = new Customer("C010","John", "pass", "john@example.com", "123", "Addr", 100, 50.00);
        repository.addCustomer(customer);

        customerAccountService.addPoints(customer, 50);

        assertEquals(150, customer.getLoyaltyPoints());
        Customer persisted = repository.findByEmail("john@example.com");
        assertEquals(150, persisted.getLoyaltyPoints());
    }

    @Test
    void testDuplicateMember() {
        // Arrange: create a customer and save it in repository
        Customer existingCustomer = new Customer("C010","John", "pass", "john@example.com", "123", "Addr", 100, 50.00);
        repository.addCustomer(existingCustomer); // assume this saves to customerRepository

        // Act & Assert: registering a customer with the same email should throw
        Customer duplicateCustomer = new Customer(null, "John2", "pass2", "john@example.com", "456", "Addr2", 0, 0.0);

        DuplicateCustomerException exception = assertThrows(DuplicateCustomerException.class, () -> {
            customerService.registerCustomer(duplicateCustomer);
        });

        // Optional: assert message
        assertTrue(exception.getMessage().contains("john@example.com"));
    }

    private static class InMemoryCustomerRepository implements CustomerRepository {
        private List<Customer> customers = new ArrayList<>();

        void addCustomer(Customer customer) {
            customers.add(customer);
        }

        Customer findByEmail(String email) {
            return customers.stream().filter(c -> c.getEmail().equals(email)).findFirst().orElse(null);
        }

        @Override
        public List<Customer> findAll() {
            return new ArrayList<>(customers);
        }

        @Override
        public void saveAll(List<Customer> customers) {
            this.customers = new ArrayList<>(customers);
        }
    }
}
