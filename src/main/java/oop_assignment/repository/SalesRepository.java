package oop_assignment.repository;

import oop_assignment.model.Sales;
import java.util.List;

/**
 * Repository for persistent storage of sales.
 */
public interface SalesRepository {

    /**
     * Finds all sales.
     * @return list of all sales
     */
    List<Sales> findAll();

    /**
     * Saves all sales to storage.
     * @param sales the list of sales to save
     */
    void saveAll(List<Sales> sales);
}
