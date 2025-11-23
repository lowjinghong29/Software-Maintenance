package oop_assignment.repository.file;

import oop_assignment.model.Staff;
import oop_assignment.repository.StaffRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * File-based implementation of StaffRepository.
 */
public class FileStaffRepository implements StaffRepository {
    private static final Logger logger = Logger.getLogger(FileStaffRepository.class.getName());
    private final Path filePath;

    public FileStaffRepository(String fileName) {
        this.filePath = Paths.get(fileName);
    }

    @Override
    public List<Staff> findAll() {
        List<Staff> staffList = new ArrayList<>();
        try {
            if (!Files.exists(filePath)) {
                return staffList; // Return empty if file doesn't exist
            }
            List<String> lines = Files.readAllLines(filePath);
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    try {
                        Staff staff = new Staff(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim());
                        staffList.add(staff);
                    } catch (Exception e) {
                        logger.warning("Malformed staff line: " + line);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading staff file: " + e.getMessage(), e);
        }
        return staffList;
    }

    @Override
    public void saveAll(List<Staff> staff) {
        List<String> lines = new ArrayList<>();
        for (Staff s : staff) {
            lines.add(s.getId() + "," + s.getName() + "," + s.getUsername() + "," + s.getPassword());
        }
        try {
            Files.write(filePath, lines);
        } catch (IOException e) {
            throw new RuntimeException("Error writing staff file: " + e.getMessage(), e);
        }
    }
}
