package oop_assignment.service;

import java.util.Map;

/**
 * Service for report operations.
 */
public interface ReportService {

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
