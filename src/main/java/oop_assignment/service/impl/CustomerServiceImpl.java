package oop_assignment.service.impl;

import oop_assignment.exception.DuplicateCustomerException;
import oop_assignment.model.Customer;
import oop_assignment.repository.CustomerRepository;
import oop_assignment.service.CustomerService;
import java.util.List;

/**
 * Implementation of CustomerService.
 */
public class CustomerServiceImpl implements CustomerService {

    private  CustomerRepository customerRepository=null;

    public CustomerServiceImpl()
    {

    }

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        if (customerRepository == null) {
            throw new IllegalArgumentException("CustomerRepository cannot be null");
        }
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer registerCustomer(Customer newCustomer) {
        List<Customer> customers = customerRepository.findAll();
        for (Customer c : customers) {
            if (c.getEmail().equalsIgnoreCase(newCustomer.getEmail())) {
                throw new DuplicateCustomerException("Customer with email " + newCustomer.getEmail() + " already exists.");
            }
        }
        // Generate new ID
        int maxId = customers.stream().mapToInt(c -> Integer.parseInt(c.getId().substring(1))).max().orElse(0);
        String newId = "C" + String.format("%03d", maxId + 1);
        newCustomer.setId(newId);
        customers.add(newCustomer);
        customerRepository.saveAll(customers);
        return newCustomer;
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public void updateCustomer(Customer customer) {
        List<Customer> customers = customerRepository.findAll();
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getId().equals(customer.getId())) {
                customers.set(i, customer);
                break;
            }
        }
        customerRepository.saveAll(customers);
    }
}
