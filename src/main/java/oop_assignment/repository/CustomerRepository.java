package oop_assignment.repository;

import oop_assignment.model.Customer;
import java.util.List;

/**
 * Repository for persistent storage of customers.
 */
public interface CustomerRepository {

    /**
     * Finds all customers.
     * @return list of all customers
     */
    List<Customer> findAll();

    /**
     * Saves all customers to storage.
     * @param customers the list of customers to save
     */
    void saveAll(List<Customer> customers);
}
