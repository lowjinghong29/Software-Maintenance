package oop_assignment.service;

import oop_assignment.model.Customer;
import oop_assignment.model.Staff;

/**
 * Service for authentication.
 */
public interface AuthService {

    /**
     * Logs in a customer.
     * @param idOrEmail the id or email
     * @param password the password
     * @return the customer
     */
    Customer customerLogin(String idOrEmail, String password);

    /**
     * Logs in a staff member.
     * @param username the username
     * @param password the password
     * @return the staff
     */
    Staff staffLogin(String username, String password);

    /**
     * Registers a new customer.
     * @param customer the customer to register
     */
    void registerCustomer(Customer customer);
}
