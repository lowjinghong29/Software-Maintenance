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

    @Override
    public void addGrocery(Groceries grocery) {
        if (grocery == null) {
            throw new InvalidInputException("Grocery cannot be null");
        }
        List<Groceries> all = groceriesRepository.findAll();
        // Check if already exists
        for (Groceries g : all) {
            if (g.getName().equals(grocery.getName())) {
                throw new InvalidInputException("Grocery with name " + grocery.getName() + " already exists");
            }
        }
        all.add(grocery);
        groceriesRepository.saveAll(all);
    }

    @Override
    public void removeGrocery(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new InvalidInputException("Id cannot be null or empty");
        }
        List<Groceries> all = groceriesRepository.findAll();
        Groceries toRemove = null;
        for (Groceries g : all) {
            if (g.getName().equals(id)) {
                toRemove = g;
                break;
            }
        }
        if (toRemove == null) {
            throw new ProductNotFoundException("Product not found for id: " + id);
        }
        all.remove(toRemove);
        groceriesRepository.saveAll(all);
    }


    @Override
    public void updateGrocery(Groceries grocery) {
        if (grocery == null) {
            throw new InvalidInputException("Grocery cannot be null");
        }
        List<Groceries> all = groceriesRepository.findAll();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getName().equals(grocery.getName())) {
                all.set(i, grocery);
                groceriesRepository.saveAll(all);
                return;
            }
        }
        throw new ProductNotFoundException("Product not found for name: " + grocery.getName());
    }

    @Override
    public List<Groceries> searchGroceries(String keyword) {
        if (keyword == null) {
            return groceriesRepository.findAll();
        }
        List<Groceries> all = groceriesRepository.findAll();
        List<Groceries> results = new java.util.ArrayList<>();
        for (Groceries g : all) {
            if (g.getName().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(g);
            }
        }
        return results;
    }
}
