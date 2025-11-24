package oop_assignment.repository.file;

import oop_assignment.exception.TrapstarException;
import oop_assignment.model.Sales;
import oop_assignment.repository.SalesRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * File-based implementation of SalesRepository.
 */
public class FileSalesRepository implements SalesRepository {

    private static final Logger logger = Logger.getLogger(FileSalesRepository.class.getName());

    private final Path filePath;

    public FileSalesRepository(String fileName) {
        this.filePath = Paths.get(fileName);
    }

    @Override
    public List<Sales> findAll() {
        List<Sales> sales = new ArrayList<>();
        if (!Files.exists(filePath)) {
            return sales;
        }
        try {
            List<String> lines = Files.readAllLines(filePath);
            for (String line : lines) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] tokens = line.split(",");
                if (tokens.length >= 7) {
                    try {
                        String saleId = tokens[0].trim();
                        String productId = tokens[1].trim();
                        String productName = tokens[2].trim();
                        int quantity = Integer.parseInt(tokens[3].trim());
                        double lineTotal = Double.parseDouble(tokens[4].trim());
                        String dateTime = tokens[5].trim();
                        String customerId = tokens[6].trim();
                        Sales sale = new Sales(saleId, productId, productName, quantity, lineTotal, dateTime, customerId);
                        sales.add(sale);
                    } catch (NumberFormatException e) {
                        logger.log(Level.WARNING, "Malformed line in sales file: " + line, e);
                    }
                } else {
                    logger.log(Level.WARNING, "Malformed line in sales file: " + line);
                }
            }
        } catch (IOException e) {
            throw new TrapstarException("Error reading sales file: " + e.getMessage(), e);
        }
        return sales;
    }

    @Override
    public void saveAll(List<Sales> sales) {
        List<String> lines = new ArrayList<>();
        for (Sales sale : sales) {
            String line = String.format("%s,%s,%s,%d,%.2f,%s,%s",
                    sale.getSaleId(),
                    sale.getProductId(),
                    sale.getProductName(),
                    sale.getQuantity(),
                    sale.getLineTotal(),
                    sale.getDateTime(),
                    sale.getCustomerId());
            lines.add(line);
        }
        try {
            Files.write(filePath, lines);
        } catch (IOException e) {
            throw new TrapstarException("Error writing sales file: " + e.getMessage(), e);
        }
    }
}
