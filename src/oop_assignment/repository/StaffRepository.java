package oop_assignment.repository;

/**
 * Interface for persistent storage of Staff entities.
 */
public interface StaffRepository {
    java.util.List<oop_assignment.model.Staff> findAll();
    void saveAll(java.util.List<oop_assignment.model.Staff> staff);
}
