package oop_assignment.model;

/**
 * Represents the result of applying pricing rules to a Cart.
 */
public class PricingSummary {
    private final double subtotal;
    private final double taxAmount;
    private final double deliveryFee;
    private final double discountAmount;
    private final double grandTotal;

    /**
     * Constructs a PricingSummary with the given values.
     * @param subtotal the subtotal
     * @param taxAmount the tax amount
     * @param deliveryFee the delivery fee
     * @param discountAmount the discount amount
     * @param grandTotal the grand total
     * @throws IllegalArgumentException if any value is negative
     */
    public PricingSummary(double subtotal, double taxAmount, double deliveryFee, double discountAmount, double grandTotal) {
        if (subtotal < 0 || taxAmount < 0 || deliveryFee < 0 || discountAmount < 0 || grandTotal < 0) {
            throw new IllegalArgumentException("Values cannot be negative");
        }
        this.subtotal = subtotal;
        this.taxAmount = taxAmount;
        this.deliveryFee = deliveryFee;
        this.discountAmount = discountAmount;
        this.grandTotal = grandTotal;
    }

    /**
     * Gets the subtotal.
     * @return the subtotal
     */
    public double getSubtotal() {
        return subtotal;
    }

    /**
     * Gets the tax amount.
     * @return the tax amount
     */
    public double getTaxAmount() {
        return taxAmount;
    }

    /**
     * Gets the delivery fee.
     * @return the delivery fee
     */
    public double getDeliveryFee() {
        return deliveryFee;
    }

    /**
     * Gets the discount amount.
     * @return the discount amount
     */
    public double getDiscountAmount() {
        return discountAmount;
    }

    /**
     * Gets the grand total.
     * @return the grand total
     */
    public double getGrandTotal() {
        return grandTotal;
    }

    @Override
    public String toString() {
        return String.format("PricingSummary{subtotal=%.2f, taxAmount=%.2f, deliveryFee=%.2f, discountAmount=%.2f, grandTotal=%.2f}",
                subtotal, taxAmount, deliveryFee, discountAmount, grandTotal);
    }
}
