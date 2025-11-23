package oop_assignment.service;

import oop_assignment.model.Cart;
import oop_assignment.model.Customer;
import oop_assignment.model.PricingSummary;
import java.util.Map;

/**
 * Service for sales operations.
 */
public interface SalesService {

    /**
     * Records a sale.
     * @param cart the cart
     * @param pricingSummary the pricing summary
     * @param customer the customer
     */
    void recordSale(Cart cart, PricingSummary pricingSummary, Customer customer);

    /**
     * Gets total revenue.
     * @return total revenue
     */
    double getTotalRevenue();

    /**
     * Gets total quantity by product id.
     * @return map of product id to total quantity
     */
    Map<String, Integer> getTotalQuantityByProductId();
}
