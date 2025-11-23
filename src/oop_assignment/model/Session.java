package oop_assignment.model;

/**
 * Model for tracking the current session state.
 */
public class Session {
    private Customer currentCustomer;

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
}
