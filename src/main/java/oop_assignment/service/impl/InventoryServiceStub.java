package oop_assignment.service.impl;

import oop_assignment.model.Cart;
import oop_assignment.model.Groceries;
import oop_assignment.service.InventoryService;
import java.util.Collections;
import java.util.List;

/**
 * Stub implementation of InventoryService.
 */
public class InventoryServiceStub implements InventoryService {
    @Override
    public List<Groceries> getAllGroceries() {
        return Collections.emptyList(); // TODO: implement
    }

    @Override
    public Groceries findById(String id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public boolean isEnoughStock(Groceries item, int quantity) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void decreaseStock(Cart cart) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void addGrocery(Groceries grocery) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void removeGrocery(String id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void increaseStock(String id, int amount) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void updateGrocery(Groceries grocery) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<Groceries> searchGroceries(String keyword) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
