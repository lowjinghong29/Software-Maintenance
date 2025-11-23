package oop_assignment.service.impl;

import oop_assignment.exception.ProductNotFoundException;
import oop_assignment.exception.OutOfStockException;
import oop_assignment.model.Cart;
import oop_assignment.model.Groceries;
import oop_assignment.repository.GroceriesRepository;
import oop_assignment.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class InventoryServiceImplTest {

    private InventoryService inventoryService;
    private InMemoryGroceriesRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryGroceriesRepository();
        inventoryService = new InventoryServiceImpl(repository);
    }

    @Test
    void testGetAllGroceries_ReturnsRepositoryData() {
        Groceries item = new Groceries("Apple", 2.50, 10);
        repository.addItem(item);

        List<Groceries> all = inventoryService.getAllGroceries();
        assertEquals(1, all.size());
        assertEquals("Apple", all.get(0).getName());
    }

    @Test
    void testFindById_ExistingId_ReturnsGroceries() {
        Groceries item = new Groceries("Apple", 2.50, 10);
        repository.addItem(item);

        Groceries found = inventoryService.findById("Apple");
        assertEquals("Apple", found.getName());
    }

    @Test
    void testFindById_NonExistingId_ThrowsProductNotFoundException() {
        assertThrows(ProductNotFoundException.class, () -> inventoryService.findById("NonExistent"));
    }

    @Test
    void testIsEnoughStock_TrueAndFalseCases() {
        Groceries item = new Groceries("Apple", 2.50, 10);
        repository.addItem(item);

        assertTrue(inventoryService.isEnoughStock(item, 5));
        assertFalse(inventoryService.isEnoughStock(item, 11));
    }

    @Test
    void testDecreaseStock_UpdatesRepositoryAndThrowsWhenInsufficient() {
        Groceries item = new Groceries("Apple", 2.50, 10);
        repository.addItem(item);
        Cart cart = new Cart();
        cart.addItem(item, 3);

        inventoryService.decreaseStock(cart);

        assertEquals(7, repository.findByName("Apple").getStockQuantity());
    }

    @Test
    void testDecreaseStock_InsufficientStock_ThrowsOutOfStockException() {
        Groceries item = new Groceries("Apple", 2.50, 5);
        repository.addItem(item);
        Cart cart = new Cart();
        cart.addItem(item, 10);

        assertThrows(OutOfStockException.class, () -> inventoryService.decreaseStock(cart));
    }

    private static class InMemoryGroceriesRepository implements GroceriesRepository {
        private List<Groceries> groceries = new ArrayList<>();

        void addItem(Groceries item) {
            groceries.add(item);
        }

        Groceries findByName(String name) {
            return groceries.stream().filter(g -> g.getName().equals(name)).findFirst().orElse(null);
        }

        @Override
        public List<Groceries> findAll() {
            return new ArrayList<>(groceries);
        }

        @Override
        public void saveAll(List<Groceries> groceries) {
            this.groceries = new ArrayList<>(groceries);
        }
    }
}
