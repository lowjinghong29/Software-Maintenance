package oop_assignment.service;

import oop_assignment.model.Customer;
import java.util.List;

/**
 * Service for customer operations.
 */
public interface CustomerService {

    /**
     * Registers a new customer.
     * @param newCustomer the new customer
     * @return the registered customer
     */
    Customer registerCustomer(Customer newCustomer);

    /**
     * Gets all customers.
     * @return list of all customers
     */
    List<Customer> getAllCustomers();

    /**
     * Updates a customer.
     * @param customer the customer to update
     */
    void updateCustomer(Customer customer);
}
