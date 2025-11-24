package oop_assignment.repository;

import oop_assignment.model.Cart;
import java.util.Map;

/**
 * Repository for persisting user carts.
 */
public interface CartRepository {

    /**
     * Saves the cart for a user.
     * @param userId the user ID
     * @param cart the cart
     */
    void saveCart(String userId, Cart cart);

    /**
     * Loads the cart for a user.
     * @param userId the user ID
     * @return the cart, or empty cart if none
     */
    Cart loadCart(String userId);
}
