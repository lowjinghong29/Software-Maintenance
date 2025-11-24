package oop_assignment.service.impl;

import oop_assignment.service.ReportService;
import oop_assignment.service.SalesService;
import java.util.Map;

/**
 * Implementation of ReportService.
 */
public class ReportServiceImpl implements ReportService {

    private final SalesService salesService;

    public ReportServiceImpl(SalesService salesService) {
        if (salesService == null) {
            throw new IllegalArgumentException("SalesService cannot be null");
        }
        this.salesService = salesService;
    }

    @Override
    public double getTotalRevenue() {
        return salesService.getTotalRevenue();
    }

    @Override
    public Map<String, Integer> getTotalQuantityByProductId() {
        return salesService.getTotalQuantityByProductId();
    }
}
