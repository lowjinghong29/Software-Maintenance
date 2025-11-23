package oop_assignment.service.impl;

import oop_assignment.exception.InvalidInputException;
import oop_assignment.exception.InvalidQuantityException;
import oop_assignment.exception.OutOfStockException;
import oop_assignment.exception.ProductNotFoundException;
import oop_assignment.model.Cart;
import oop_assignment.model.CartItem;
import oop_assignment.model.Groceries;
import oop_assignment.repository.GroceriesRepository;
import oop_assignment.service.InventoryService;
import java.util.List;

/**
 * Implementation of InventoryService.
 */
public class InventoryServiceImpl implements InventoryService {

    private final GroceriesRepository groceriesRepository;

    public InventoryServiceImpl(GroceriesRepository groceriesRepository) {
        if (groceriesRepository == null) {
            throw new IllegalArgumentException("GroceriesRepository cannot be null");
        }
        this.groceriesRepository = groceriesRepository;
    }

    @Override
    public List<Groceries> getAllGroceries() {
        return groceriesRepository.findAll();
    }

    @Override
    public Groceries findById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new InvalidInputException("Id cannot be null or empty");
        }
        List<Groceries> all = groceriesRepository.findAll();
        for (Groceries grocery : all) {
            if (grocery.getName().equals(id)) {
                return grocery;
            }
        }
        throw new ProductNotFoundException("Product not found for id: " + id);
    }

    @Override
    public boolean isEnoughStock(Groceries item, int quantity) {
        if (item == null) {
            throw new InvalidInputException("Item cannot be null");
        }
        if (quantity <= 0) {
            throw new InvalidQuantityException("Quantity must be > 0");
        }
        return item.getStockQuantity() >= quantity;
    }

    @Override
    public void decreaseStock(Cart cart) {
        if (cart == null || cart.isEmpty()) {
            throw new InvalidInputException("Cart cannot be null or empty");
        }
        List<Groceries> allGroceries = groceriesRepository.findAll();
        for (CartItem cartItem : cart.getItems()) {
            Groceries item = cartItem.getItem();
            int quantity = cartItem.getQuantity();
            // Find the item in the list
            Groceries groceryToUpdate = null;
            for (Groceries g : allGroceries) {
                if (g.getName().equals(item.getName())) {
                    groceryToUpdate = g;
                    break;
                }
            }
            if (groceryToUpdate == null) {
                throw new ProductNotFoundException("Product not found: " + item.getName());
            }
            if (groceryToUpdate.getStockQuantity() < quantity) {
                throw new OutOfStockException("Not enough stock for product: " + item.getName());
            }
            groceryToUpdate.setStockQuantity(groceryToUpdate.getStockQuantity() - quantity);
        }
        groceriesRepository.saveAll(allGroceries);
    }
}
