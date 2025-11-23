package oop_assignment.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CartTest {

    @Test
    void testAddItem_NewItem_AddsToCart() {
        Cart cart = new Cart();
        Groceries item = new Groceries("Apple", 2.50, 10);
        cart.addItem(item, 2);

        assertFalse(cart.isEmpty());
        assertEquals(1, cart.getItems().size());
        assertEquals(2, cart.getTotalQuantity());
        assertEquals(5.00, cart.getSubtotal(), 0.01);
    }

    @Test
    void testAddItem_SameItem_IncreasesQuantity() {
        Cart cart = new Cart();
        Groceries item = new Groceries("Apple", 2.50, 10);
        cart.addItem(item, 2);
        cart.addItem(item, 3);

        assertEquals(1, cart.getItems().size());
        assertEquals(5, cart.getTotalQuantity());
        assertEquals(12.50, cart.getSubtotal(), 0.01);
    }

    @Test
    void testRemoveItem_RemovesCorrectItem() {
        Cart cart = new Cart();
        Groceries item1 = new Groceries("Apple", 2.50, 10);
        Groceries item2 = new Groceries("Banana", 1.00, 5);
        cart.addItem(item1, 1);
        cart.addItem(item2, 1);
        cart.removeItem(item1);

        assertEquals(1, cart.getItems().size());
        assertEquals("Banana", cart.getItems().get(0).getItem().getName());
    }

    @Test
    void testGetSubtotal_EmptyCart_ReturnsZero() {
        Cart cart = new Cart();
        assertEquals(0.0, cart.getSubtotal(), 0.01);
    }
}
