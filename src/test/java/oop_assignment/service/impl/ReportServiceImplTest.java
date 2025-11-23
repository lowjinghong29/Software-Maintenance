package oop_assignment.service.impl;

import oop_assignment.service.ReportService;
import oop_assignment.service.SalesService;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class ReportServiceImplTest {

    @Test
    void testGetTotalRevenue_DelegatesToSalesService() {
        StubSalesService salesService = new StubSalesService();
        ReportService reportService = new ReportServiceImpl(salesService);

        double total = reportService.getTotalRevenue();
        assertEquals(100.00, total, 0.01);
    }

    @Test
    void testGetTotalQuantityByProductId_DelegatesToSalesService() {
        StubSalesService salesService = new StubSalesService();
        ReportService reportService = new ReportServiceImpl(salesService);

        Map<String, Integer> map = reportService.getTotalQuantityByProductId();
        assertEquals(10, map.get("Apple"));
        assertEquals(5, map.get("Banana"));
    }

    private static class StubSalesService implements SalesService {
        @Override
        public void recordSale(Cart cart, PricingSummary pricingSummary, Customer customer) {
        }

        @Override
        public double getTotalRevenue() {
            return 100.00;
        }

        @Override
        public Map<String, Integer> getTotalQuantityByProductId() {
            return Map.of("Apple", 10, "Banana", 5);
        }
    }
}
