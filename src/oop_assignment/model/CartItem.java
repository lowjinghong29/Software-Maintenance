package oop_assignment.model;

/**
 * Represents a single line item in the shopping cart.
 */
public class CartItem {
    private final Groceries item;
    private int quantity;

    /**
     * Constructs a CartItem with the given item and quantity.
     * @param item the groceries item, must not be null
     * @param quantity the quantity, must be > 0
     * @throws IllegalArgumentException if item is null or quantity <= 0
     */
    public CartItem(Groceries item, int quantity) {
        if (item == null || quantity <= 0) {
            throw new IllegalArgumentException("Invalid item or quantity");
        }
        this.item = item;
        this.quantity = quantity;
    }

    /**
     * Gets the groceries item.
     * @return the item
     */
    public Groceries getItem() {
        return item;
    }

    /**
     * Gets the quantity.
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity, enforcing quantity > 0.
     * @param quantity the new quantity, must be > 0
     * @throws IllegalArgumentException if quantity <= 0
     */
    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be > 0");
        }
        this.quantity = quantity;
    }

    /**
     * Calculates the line total for this item.
     * @return item price multiplied by quantity
     */
    public double getLineTotal() {
        return item.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return String.format("CartItem{item=%s, quantity=%d, lineTotal=%.2f}", item.getName(), quantity, getLineTotal());
    }
}
