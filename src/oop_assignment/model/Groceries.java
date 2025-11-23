package oop_assignment.model;

/**
 * Represents a groceries item in the system.
 */
public class Groceries {
    private String name;
    private double price;
    private int stockQuantity;

    // Constructors, getters, setters

    public Groceries(String name, double price, int stockQuantity) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Groceries groceries = (Groceries) obj;
        return name != null ? name.equals(groceries.name) : groceries.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Groceries{name='" + name + "', price=" + price + ", stockQuantity=" + stockQuantity + "}";
    }
}
