package oop_assignment.repository.file;

import oop_assignment.exception.TrapstarException;
import oop_assignment.model.Groceries;
import oop_assignment.repository.GroceriesRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * File-based implementation of GroceriesRepository.
 */
public class FileGroceriesRepository implements GroceriesRepository {

    private static final Logger logger = Logger.getLogger(FileGroceriesRepository.class.getName());

    private final Path filePath;

    public FileGroceriesRepository(String fileName) {
        this.filePath = Paths.get(fileName);
    }

    @Override
    public List<Groceries> findAll() {
        List<Groceries> groceries = new ArrayList<>();
        if (!Files.exists(filePath)) {
            return groceries;
        }
        try {
            List<String> lines = Files.readAllLines(filePath);
            for (String line : lines) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] tokens = line.split("\\|");
                if (tokens.length >= 3) {
                    try {
                        String name = tokens[0].trim();
                        double price = Double.parseDouble(tokens[1].trim());
                        int stock = Integer.parseInt(tokens[2].trim());
                        Groceries grocery = new Groceries(name, price, stock);
                        groceries.add(grocery);
                    } catch (NumberFormatException e) {
                        logger.log(Level.WARNING, "Malformed line in groceries file: " + line, e);
                    }
                } else {
                    logger.log(Level.WARNING, "Malformed line in groceries file: " + line);
                }
            }
        } catch (IOException e) {
            throw new TrapstarException("Error reading groceries file: " + e.getMessage(), e);
        }
        return groceries;
    }

    @Override
    public void saveAll(List<Groceries> groceries) {
        List<String> lines = new ArrayList<>();
        for (Groceries grocery : groceries) {
            String line = String.format("%s|%.2f|%d", grocery.getName(), grocery.getPrice(), grocery.getStockQuantity());
            lines.add(line);
        }
        try {
            Files.write(filePath, lines);
        } catch (IOException e) {
            throw new TrapstarException("Error writing groceries file: " + e.getMessage(), e);
        }
    }
}
