package oop_assignment.service.impl;

import oop_assignment.exception.AuthenticationFailedException;
import oop_assignment.exception.InvalidInputException;
import oop_assignment.model.Customer;
import oop_assignment.model.Staff;
import oop_assignment.repository.CustomerRepository;
import oop_assignment.repository.StaffRepository;
import oop_assignment.service.AuthService;
import oop_assignment.service.CustomerService;
import java.util.List;

/**
 * Implementation of AuthService.
 */
public class AuthServiceImpl implements AuthService {

    private final CustomerRepository customerRepository;
    private final StaffRepository staffRepository;
    private final CustomerService customerService;

    public AuthServiceImpl(CustomerRepository customerRepository, StaffRepository staffRepository, CustomerService customerService) {
        if (customerRepository == null) {
            throw new IllegalArgumentException("CustomerRepository cannot be null");
        }
        if (staffRepository == null) {
            throw new IllegalArgumentException("StaffRepository cannot be null");
        }
        if (customerService == null) {
            throw new IllegalArgumentException("CustomerService cannot be null");
        }
        this.customerRepository = customerRepository;
        this.staffRepository = staffRepository;
        this.customerService = customerService;
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

    @Override
    public Staff staffLogin(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null) {
            throw new InvalidInputException("Invalid credentials");
        }
        List<Staff> all = staffRepository.findAll();
        for (Staff s : all) {
            if (s.getUsername().equals(username.trim()) && s.getPassword().equals(password)) {
                return s;
            }
        }
        throw new AuthenticationFailedException("Invalid credentials");
    }

    @Override
    public void registerCustomer(Customer customer) {
        customerService.registerCustomer(customer);
    }
}
