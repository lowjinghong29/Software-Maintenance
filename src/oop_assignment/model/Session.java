package oop_assignment.model;

/**
 * Model for tracking the current session state.
 */
public class Session {
    private Customer currentCustomer;
    private Staff currentStaff;
    private Cart currentCart;

    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    public void setCurrentCustomer(Customer currentCustomer) {
        this.currentCustomer = currentCustomer;
    }

    public boolean hasCustomer() {
        return currentCustomer != null;
    }

    public void clearCustomer() {
        this.currentCustomer = null;
    }

    public Staff getCurrentStaff() {
        return currentStaff;
    }

    public void setCurrentStaff(Staff currentStaff) {
        this.currentStaff = currentStaff;
    }

    public Cart getCurrentCart() {
        return currentCart;
    }

    public void setCurrentCart(Cart currentCart) {
        this.currentCart = currentCart;
    }
}
