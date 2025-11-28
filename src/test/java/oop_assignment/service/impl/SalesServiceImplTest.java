package oop_assignment.service.impl;

import oop_assignment.model.Cart;
import oop_assignment.model.Groceries;
import oop_assignment.model.PricingSummary;
import oop_assignment.model.Sales;
import oop_assignment.repository.SalesRepository;
import oop_assignment.service.SalesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class SalesServiceImplTest {

    private SalesService salesService;
    private InMemorySalesRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemorySalesRepository();
        salesService = new SalesServiceImpl(repository);
    }

    @Test
    void testRecordSale_AddsEntriesToRepository() {
        Cart cart = new Cart();
        Groceries item1 = new Groceries("Apple", 5.00, 10);
        Groceries item2 = new Groceries("Banana", 10.00, 5);
        cart.addItem(item1, 2);
        cart.addItem(item2, 1);
        PricingSummary pricingSummary = new PricingSummary(20.00, 1.20, 4.90, 0.0, 26.10);

        salesService.recordSale(cart, pricingSummary, null);

        assertEquals(2, repository.findAll().size());
    }

    @Test
    void testGetTotalRevenue_SumsLineTotals() {
        Sales sale1 = new Sales(null, "Apple", "Apple", 2, 10.00, "2023-01-01", null);
        Sales sale2 = new Sales(null, "Banana", "Banana", 1, 10.00, "2023-01-01", null);
        repository.addSale(sale1);
        repository.addSale(sale2);

        double total = salesService.getTotalRevenue();
        assertEquals(20.00, total, 0.01);
    }

    @Test
    void testGetTotalQuantityByProductId_GroupsQuantitiesCorrectly() {
        Sales sale1 = new Sales(null, "Apple", "Apple", 2, 10.00, "2023-01-01", null);
        Sales sale2 = new Sales(null, "Apple", "Apple", 3, 15.00, "2023-01-01", null);
        Sales sale3 = new Sales(null, "Banana", "Banana", 1, 10.00, "2023-01-01", null);
        repository.addSale(sale1);
        repository.addSale(sale2);
        repository.addSale(sale3);

        Map<String, Integer> map = salesService.getTotalQuantityByProductId();
        assertEquals(5, map.get("Apple"));
        assertEquals(1, map.get("Banana"));
    }

    private static class InMemorySalesRepository implements SalesRepository {
        private List<Sales> sales = new ArrayList<>();

        void addSale(Sales sale) {
            sales.add(sale);
        }

        @Override
        public List<Sales> findAll() {
            return new ArrayList<>(sales);
        }

        @Override
        public void saveAll(List<Sales> sales) {
            this.sales = new ArrayList<>(sales);
        }
    }
}
