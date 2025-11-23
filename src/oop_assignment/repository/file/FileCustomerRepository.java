package oop_assignment.repository.file;

import oop_assignment.exception.TrapstarException;
import oop_assignment.model.Customer;
import oop_assignment.repository.CustomerRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * File-based implementation of CustomerRepository.
 */
public class FileCustomerRepository implements CustomerRepository {

    private static final Logger logger = Logger.getLogger(FileCustomerRepository.class.getName());

    private final Path filePath;

    public FileCustomerRepository(String fileName) {
        this.filePath = Paths.get(fileName);
    }

    @Override
    public List<Customer> findAll() {
        List<Customer> customers = new ArrayList<>();
        if (!Files.exists(filePath)) {
            return customers;
        }
        try {
            List<String> lines = Files.readAllLines(filePath);
            for (String line : lines) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] tokens = line.split("\\|");
                if (tokens.length >= 7) {
                    try {
                        String name = tokens[0].trim();
                        String password = tokens[1].trim();
                        String email = tokens[2].trim();
                        String phoneNumber = tokens[3].trim();
                        String mailingAddress = tokens[4].trim();
                        int loyaltyPoints = Integer.parseInt(tokens[5].trim());
                        double balance = Double.parseDouble(tokens[6].trim());
                        Customer customer = new Customer(name, password, email, phoneNumber, mailingAddress, loyaltyPoints, balance);
                        customers.add(customer);
                    } catch (NumberFormatException e) {
                        logger.log(Level.WARNING, "Malformed line in customers file: " + line, e);
                    }
                } else {
                    logger.log(Level.WARNING, "Malformed line in customers file: " + line);
                }
            }
        } catch (IOException e) {
            throw new TrapstarException("Error reading customers file: " + e.getMessage(), e);
        }
        return customers;
    }

    @Override
    public void saveAll(List<Customer> customers) {
        List<String> lines = new ArrayList<>();
        for (Customer customer : customers) {
            String line = String.format("%s|%s|%s|%s|%s|%d|%.2f",
                    customer.getName(),
                    customer.getPassword(),
                    customer.getEmail(),
                    customer.getPhoneNumber(),
                    customer.getMailingAddress(),
                    customer.getLoyaltyPoints(),
                    customer.getBalance());
            lines.add(line);
        }
        try {
            Files.write(filePath, lines);
        } catch (IOException e) {
            throw new TrapstarException("Error writing customers file: " + e.getMessage(), e);
        }
    }
}
