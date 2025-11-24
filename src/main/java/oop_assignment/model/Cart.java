package oop_assignment.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the shopping cart for a single checkout session.
 */
public class Cart {
    private final List<CartItem> items = new ArrayList<>();

    /**
     * Constructs an empty cart.
     */
    public Cart() {
    }

    /**
     * Adds an item to the cart. If the item already exists, increases its quantity.
     * @param item the groceries item, must not be null
     * @param quantity the quantity to add, must be > 0
     * @throws IllegalArgumentException if item is null or quantity <= 0
     */
    public void addItem(Groceries item, int quantity) {
        if (item == null || quantity <= 0) {
            throw new IllegalArgumentException("Invalid item or quantity");
        }
        for (CartItem cartItem : items) {
            if (cartItem.getItem().equals(item)) {
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                return;
            }
        }
        items.add(new CartItem(item, quantity));
    }

    /**
     * Removes the item from the cart if present.
     * @param item the groceries item to remove, may be null (no-op if null)
     */
    public void removeItem(Groceries item) {
        if (item == null) {
            return;
        }
        items.removeIf(cartItem -> cartItem.getItem().equals(item));
    }

    /**
     * Gets an unmodifiable view of the cart items.
     * @return the list of cart items
     */
    public List<CartItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    /**
     * Checks if the cart is empty.
     * @return true if no items, false otherwise
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * Gets the total quantity of all items in the cart.
     * @return the sum of quantities
     */
    public int getTotalQuantity() {
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }

    /**
     * Gets the subtotal of all items in the cart.
     * @return the sum of line totals
     */
    public double getSubtotal() {
        return items.stream().mapToDouble(CartItem::getLineTotal).sum();
    }
}
