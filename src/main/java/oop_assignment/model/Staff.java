package oop_assignment.model;

/**
 * Represents a staff member in the system.
 */
public class Staff {
    private String id;
    private String name;
    private String username;
    private String password;

    public Staff(String id, String name, String username, String password) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
