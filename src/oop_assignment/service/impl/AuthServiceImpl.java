package oop_assignment.service.impl;

import oop_assignment.exception.AuthenticationFailedException;
import oop_assignment.exception.InvalidInputException;
import oop_assignment.model.Customer;
import oop_assignment.repository.CustomerRepository;
import oop_assignment.service.AuthService;
import java.util.List;

/**
 * Implementation of AuthService.
 */
public class AuthServiceImpl implements AuthService {

    private final CustomerRepository customerRepository;

    public AuthServiceImpl(CustomerRepository customerRepository) {
        if (customerRepository == null) {
            throw new IllegalArgumentException("CustomerRepository cannot be null");
        }
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer customerLogin(String idOrEmail, String password) {
        if (idOrEmail == null || idOrEmail.trim().isEmpty() || password == null) {
            throw new InvalidInputException("Invalid credentials");
        }
        List<Customer> all = customerRepository.findAll();
        for (Customer c : all) {
            if (c.getEmail().equalsIgnoreCase(idOrEmail.trim())) {
                if (c.getPassword().equals(password)) {
                    return c;
                } else {
                    throw new AuthenticationFailedException("Invalid credentials");
                }
            }
        }
        throw new AuthenticationFailedException("Invalid credentials");
    }
}
