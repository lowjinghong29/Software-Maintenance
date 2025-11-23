package oop_assignment.model;

import java.util.Map;

/**
 * Represents a customer in the system.
 */
public class Customer {
    private String id;
    private String name;
    private String password;
    private String email;
    private String phoneNumber;
    private String mailingAddress;
    private int loyaltyPoints;
    private double balance;
    private double discountAmount;
    private Map<String, Integer> discountVouchers;
    private String appliedVoucher;

    // Constructors, getters, setters

    public Customer(String id, String name, String password, String email, String phoneNumber, String mailingAddress, int loyaltyPoints, double balance, double discountAmount) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.mailingAddress = mailingAddress;
        this.loyaltyPoints = loyaltyPoints;
        this.balance = balance;
        this.discountAmount = discountAmount;
        this.discountVouchers = new java.util.HashMap<>();
    }

    public Customer(String id, String name, String password, String email, String phoneNumber, String mailingAddress, int loyaltyPoints, double balance) {
        this(id, name, password, email, phoneNumber, mailingAddress, loyaltyPoints, balance, 0.0);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Map<String, Integer> getDiscountVouchers() {
        return discountVouchers;
    }

    public void setDiscountVouchers(Map<String, Integer> discountVouchers) {
        this.discountVouchers = discountVouchers;
    }

    public String getAppliedVoucher() {
        return appliedVoucher;
    }

    public void setAppliedVoucher(String appliedVoucher) {
        this.appliedVoucher = appliedVoucher;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Customer customer = (Customer) obj;
        return id != null ? id.equals(customer.id) : customer.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", loyaltyPoints=" + loyaltyPoints +
                ", balance=" + balance +
                ", discountAmount=" + discountAmount +
                '}';
    }
}
