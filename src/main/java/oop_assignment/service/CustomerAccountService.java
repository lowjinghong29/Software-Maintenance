package oop_assignment.service;

import oop_assignment.model.Customer;

/**
 * Service for customer account management.
 */
public interface CustomerAccountService {

    /**
     * Checks if the customer has enough balance.
     * @param customer the customer
     * @param amount the amount
     * @return true if enough, false otherwise
     */
    boolean hasEnoughBalance(Customer customer, double amount);

    /**
     * Debits the balance.
     * @param customer the customer
     * @param amount the amount
     * @throws InsufficientBalanceException if insufficient balance
     */
    void debitBalance(Customer customer, double amount);

    /**
     * Adds balance to the customer.
     * @param customer the customer
     * @param amount the amount
     */
    void addBalance(Customer customer, double amount);

    /**
     * Adds points to the customer.
     * @param customer the customer
     * @param points the points
     */
    void addPoints(Customer customer, int points);
}
