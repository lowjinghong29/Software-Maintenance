package oop_assignment.service.impl;

import oop_assignment.exception.InsufficientBalanceException;
import oop_assignment.exception.InvalidInputException;
import oop_assignment.model.Customer;
import oop_assignment.repository.CustomerRepository;
import oop_assignment.service.CustomerAccountService;
import java.util.List;

/**
 * Implementation of CustomerAccountService.
 */
public class CustomerAccountServiceImpl implements CustomerAccountService {

    private final CustomerRepository customerRepository;

    public CustomerAccountServiceImpl(CustomerRepository customerRepository) {
        if (customerRepository == null) {
            throw new IllegalArgumentException("CustomerRepository cannot be null");
        }
        this.customerRepository = customerRepository;
    }

    @Override
    public boolean hasEnoughBalance(Customer customer, double amount) {
        if (customer == null) {
            throw new InvalidInputException("Customer cannot be null");
        }
        if (amount < 0) {
            throw new InvalidInputException("Amount cannot be negative");
        }
        return customer.getBalance() >= amount;
    }

    @Override
    public void debitBalance(Customer customer, double amount) {
        if (customer == null || amount < 0) {
            throw new InvalidInputException("Invalid customer or amount");
        }
        if (!hasEnoughBalance(customer, amount)) {
            throw new InsufficientBalanceException("Insufficient balance");
        }
        customer.setBalance(customer.getBalance() - amount);
        updateCustomerInRepository(customer);
    }

    @Override
    public void addBalance(Customer customer, double amount) {
        if (customer == null || amount <= 0) {
            throw new InvalidInputException("Invalid customer or amount");
        }
        customer.setBalance(customer.getBalance() + amount);
        updateCustomerInRepository(customer);
    }

    @Override
    public void addPoints(Customer customer, int points) {
        if (customer == null || points < 0) {
            throw new InvalidInputException("Invalid customer or points");
        }
        customer.setLoyaltyPoints(customer.getLoyaltyPoints() + points);
        updateCustomerInRepository(customer);
    }

    private void updateCustomerInRepository(Customer customer) {
        List<Customer> all = customerRepository.findAll();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getEmail().equals(customer.getEmail())) {
                all.set(i, customer);
                customerRepository.saveAll(all);
                return;
            }
        }
        throw new RuntimeException("Customer not found in repository for update");
    }
}
