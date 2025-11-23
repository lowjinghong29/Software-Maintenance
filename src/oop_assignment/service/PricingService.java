package oop_assignment.service;

import oop_assignment.model.Cart;
import oop_assignment.model.PricingSummary;

/**
 * Service interface for calculating pricing details for a given cart and membership status.
 */
public interface PricingService {

    /**
     * Calculates the pricing summary for the given cart and membership status.
     * @param cart the shopping cart, must not be null
     * @param isMember true if the customer is a member, false otherwise
     * @return the pricing summary
     * @throws IllegalArgumentException if cart is null
     */
    PricingSummary calculate(Cart cart, boolean isMember);
}
