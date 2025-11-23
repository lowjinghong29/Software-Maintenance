package oop_assignment.service;

import oop_assignment.menu.PaymentMethod;
import oop_assignment.model.Cart;
import oop_assignment.model.CheckoutResult;
import oop_assignment.model.Customer;

/**
 * Service for orchestrating checkout.
 */
public interface CheckoutService {

    /**
     * Performs checkout.
     * @param cart the cart
     * @param customer the customer
     * @param isMember true if member
     * @param paymentMethod the payment method
     * @return the checkout result
     */
    CheckoutResult checkout(Cart cart, Customer customer, boolean isMember, PaymentMethod paymentMethod);
}
