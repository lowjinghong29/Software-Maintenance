package oop_assignment.service;

import oop_assignment.model.Customer;

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
}
