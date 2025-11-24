package oop_assignment.repository;

import oop_assignment.model.Groceries;
import java.util.List;

/**
 * Repository for persistent storage of groceries.
 */
public interface GroceriesRepository {

    /**
     * Finds all groceries.
     * @return list of all groceries
     */
    List<Groceries> findAll();

    /**
     * Saves all groceries to storage.
     * @param groceries the list of groceries to save
     */
    void saveAll(List<Groceries> groceries);
}
