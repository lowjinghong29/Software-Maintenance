package oop_assignment.model;

/**
 * Represents a customer in the system.
 */
public class Customer {
    private String name;
    private String password;
    private String email;
    private String phoneNumber;
    private String mailingAddress;
    private int loyaltyPoints;
    private double balance;

    // Constructors, getters, setters

    public Customer(String name, String password, String email, String phoneNumber, String mailingAddress, int loyaltyPoints, double balance) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.mailingAddress = mailingAddress;
        this.loyaltyPoints = loyaltyPoints;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(String mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Customer{name='" + name + "', email='" + email + "', balance=" + balance + ", loyaltyPoints=" + loyaltyPoints + "}";
    }
}
