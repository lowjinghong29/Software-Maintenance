package oop_assignment.constant;

/**
 * Utility class holding system-level constants for pricing and loyalty rules.
 */
public final class SystemConstants {

    // Private constructor to prevent instantiation
    private SystemConstants() {
    }

    /** Tax rate applied to subtotal (6%). */
    public static final double TAX_RATE = 0.06;

    /** Delivery fee for non-members. */
    public static final double DELIVERY_FEE_NON_MEMBER = 4.90;

    /** Delivery fee for members (free). */
    public static final double DELIVERY_FEE_MEMBER = 0.00;

    /** Points earned per RM spent. */
    public static final int POINTS_PER_RINGGIT = 1;
}
