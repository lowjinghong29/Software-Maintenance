package oop_assignment.service;

import oop_assignment.model.Cart;
import oop_assignment.model.Groceries;
import oop_assignment.exception.ProductNotFoundException;
import java.util.List;

/**
 * Service for inventory management.
 */
public interface InventoryService {

    /**
     * Gets all groceries.
     * @return list of all groceries
     */
    List<Groceries> getAllGroceries();

    /**
     * Finds a groceries item by id.
     * @param id the id
     * @return the groceries item
     * @throws ProductNotFoundException if not found
     */
    Groceries findById(String id);

    /**
     * Checks if there is enough stock for the item and quantity.
     * @param item the groceries item
     * @param quantity the quantity
     * @return true if enough stock, false otherwise
     */
    boolean isEnoughStock(Groceries item, int quantity);

    /**
     * Decreases stock for items in the cart.
     * @param cart the cart
     */
    void decreaseStock(Cart cart);
}
