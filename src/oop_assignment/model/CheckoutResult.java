package oop_assignment.model;

/**
 * Represents the outcome of a checkout operation.
 */
public class CheckoutResult {
    private final Cart cart;
    private final PricingSummary pricingSummary;
    private final Customer customer;

    /**
     * Constructs a CheckoutResult with the given cart, pricing summary, and customer.
     * @param cart the cart
     * @param pricingSummary the pricing summary
     * @param customer the customer, may be null
     */
    public CheckoutResult(Cart cart, PricingSummary pricingSummary, Customer customer) {
        this.cart = cart;
        this.pricingSummary = pricingSummary;
        this.customer = customer;
    }

    /**
     * Gets the cart.
     * @return the cart
     */
    public Cart getCart() {
        return cart;
    }

    /**
     * Gets the pricing summary.
     * @return the pricing summary
     */
    public PricingSummary getPricingSummary() {
        return pricingSummary;
    }

    /**
     * Gets the customer.
     * @return the customer, may be null
     */
    public Customer getCustomer() {
        return customer;
    }

    @Override
    public String toString() {
        return String.format("CheckoutResult{cart=%s, pricingSummary=%s, customer=%s}", cart, pricingSummary, customer);
    }
}
