package oop_assignment.repository.file;

import oop_assignment.exception.TrapstarException;
import oop_assignment.model.Cart;
import oop_assignment.model.CartItem;
import oop_assignment.model.Groceries;
import oop_assignment.repository.CartRepository;
import oop_assignment.service.InventoryService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * File-based implementation of CartRepository.
 */
public class FileCartRepository implements CartRepository {

    private final Path filePath;
    private final InventoryService inventoryService;

    public FileCartRepository(String fileName, InventoryService inventoryService) {
        this.filePath = Paths.get(fileName);
        this.inventoryService = inventoryService;
    }

    @Override
    public void saveCart(String userId, Cart cart) {
        try {
            List<String> lines = Files.exists(filePath) ? Files.readAllLines(filePath) : new ArrayList<>();
            Map<String, String> cartData = new HashMap<>();
            for (String line : lines) {
                if (line.contains(":")) {
                    String[] parts = line.split(":", 2);
                    cartData.put(parts[0], parts[1]);
                }
            }
            StringBuilder sb = new StringBuilder();
            for (CartItem item : cart.getItems()) {
                if (sb.length() > 0) sb.append(";");
                sb.append(item.getItem().getName()).append(",").append(item.getQuantity());
            }
            cartData.put(userId, sb.toString());
            List<String> newLines = new ArrayList<>();
            for (Map.Entry<String, String> entry : cartData.entrySet()) {
                newLines.add(entry.getKey() + ":" + entry.getValue());
            }
            Files.write(filePath, newLines);
        } catch (IOException e) {
            throw new TrapstarException("Error saving cart: " + e.getMessage(), e);
        }
    }

    @Override
    public Cart loadCart(String userId) {
        Cart cart = new Cart();
        if (!Files.exists(filePath)) {
            return cart;
        }
        try {
            List<String> lines = Files.readAllLines(filePath);
            for (String line : lines) {
                if (line.startsWith(userId + ":")) {
                    String data = line.substring(userId.length() + 1);
                    if (!data.isEmpty()) {
                        String[] items = data.split(";");
                        for (String itemStr : items) {
                            String[] parts = itemStr.split(",");
                            if (parts.length == 2) {
                                String name = parts[0];
                                int quantity = Integer.parseInt(parts[1]);
                                // Find the grocery
                                Groceries grocery = inventoryService.findById(name);
                                if (grocery != null) {
                                    cart.addItem(grocery, quantity);
                                }
                            }
                        }
                    }
                    break;
                }
            }
        } catch (IOException e) {
            // ignore, return empty cart
        }
        return cart;
    }
}
