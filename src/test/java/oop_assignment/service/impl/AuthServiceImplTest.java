package oop_assignment.service.impl;

import oop_assignment.exception.AuthenticationFailedException;
import oop_assignment.model.Customer;
import oop_assignment.repository.CustomerRepository;
import oop_assignment.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AuthServiceImplTest {

    private AuthService authService;
    private InMemoryCustomerRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryCustomerRepository();
        authService = new AuthServiceImpl(repository);
    }

    @Test
    void testCustomerLogin_ValidCredentials_ReturnsCustomer() {
        Customer customer = new Customer("John", "pass123", "john@example.com", "123", "Addr", 100, 100.00);
        repository.addCustomer(customer);

        Customer loggedIn = authService.customerLogin("john@example.com", "pass123");

        assertEquals(customer, loggedIn);
    }

    @Test
    void testCustomerLogin_InvalidId_ThrowsAuthenticationFailedException() {
        assertThrows(AuthenticationFailedException.class, () -> authService.customerLogin("invalid@example.com", "pass123"));
    }

    @Test
    void testCustomerLogin_InvalidPassword_ThrowsAuthenticationFailedException() {
        Customer customer = new Customer("John", "pass123", "john@example.com", "123", "Addr", 100, 100.00);
        repository.addCustomer(customer);

        assertThrows(AuthenticationFailedException.class, () -> authService.customerLogin("john@example.com", "wrongpass"));
    }

    private static class InMemoryCustomerRepository implements CustomerRepository {
        private List<Customer> customers = new ArrayList<>();

        void addCustomer(Customer customer) {
            customers.add(customer);
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
