package oop_assignment.service.impl;

import oop_assignment.constant.SystemConstants;
import oop_assignment.model.Cart;
import oop_assignment.model.CartItem;
import oop_assignment.model.Customer;
import oop_assignment.model.PricingSummary;
import oop_assignment.service.PricingService;

/**
 * Implementation of PricingService for calculating pricing details.
 */
public class PricingServiceImpl implements PricingService {

    @Override
    public PricingSummary calculate(Cart cart, boolean isMember, Customer customer) {
        if (cart == null) {
            throw new IllegalArgumentException("Cart must not be null");
        }

        double subtotal = 0.0;
        for (CartItem item : cart.getItems()) {
            subtotal += item.getLineTotal();
        }

        double taxAmount = subtotal * SystemConstants.TAX_RATE;
        double deliveryFee = isMember ? SystemConstants.DELIVERY_FEE_MEMBER : SystemConstants.DELIVERY_FEE_NON_MEMBER;
        double discountAmount = (customer != null) ? customer.getDiscountAmount() : 0.0;
        double grandTotal = subtotal + taxAmount + deliveryFee - discountAmount;

        return new PricingSummary(subtotal, taxAmount, deliveryFee, discountAmount, grandTotal);
    }
}
