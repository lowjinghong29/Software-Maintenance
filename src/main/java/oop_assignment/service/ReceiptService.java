package oop_assignment.service;

import oop_assignment.model.Cart;
import oop_assignment.model.Customer;
import oop_assignment.model.PricingSummary;

/**
 * Service for generating receipts.
 */
public interface ReceiptService {

    /**
     * Generates a receipt string.
     * @param cart the cart
     * @param pricingSummary the pricing summary
     * @param customer the customer
     * @return the receipt string
     */
    String generateReceipt(Cart cart, PricingSummary pricingSummary, Customer customer);
}
