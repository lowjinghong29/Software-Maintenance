package oop_assignment.service.impl;

import oop_assignment.exception.DuplicateCustomerException;
import oop_assignment.exception.CustomerNotFoundException;
import oop_assignment.model.Customer;
import oop_assignment.repository.CustomerRepository;
import oop_assignment.service.CustomerService;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of CustomerService.
 */
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        if (customerRepository == null) {
            throw new IllegalArgumentException("CustomerRepository cannot be null");
        }
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer registerCustomer(Customer newCustomer) {
        List<Customer> all = customerRepository.findAll();
        for (Customer c : all) {
            if (c.getEmail().equals(newCustomer.getEmail())) {
                throw new DuplicateCustomerException("Customer with email " + newCustomer.getEmail() + " already exists");
            }
        }
        all.add(newCustomer);
        customerRepository.saveAll(all);
        return newCustomer;
    }

    @Override
    public Customer findCustomerById(String customerId) {
        List<Customer> all = customerRepository.findAll();
        for (Customer c : all) {
            if (c.getEmail().equals(customerId)) {
                return c;
            }
        }
        throw new CustomerNotFoundException("Customer not found for id: " + customerId);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public void updateCustomer(Customer customer) {
        List<Customer> all = customerRepository.findAll();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getEmail().equals(customer.getEmail())) {
                all.set(i, customer);
                customerRepository.saveAll(all);
                return;
            }
        }
        throw new CustomerNotFoundException("Customer not found for update: " + customer.getEmail());
    }
}
