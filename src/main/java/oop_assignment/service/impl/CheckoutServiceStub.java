package oop_assignment.service.impl;

import oop_assignment.menu.PaymentMethod;
import oop_assignment.model.Cart;
import oop_assignment.model.CheckoutResult;
import oop_assignment.model.Customer;
import oop_assignment.service.CheckoutService;

/**
 * Stub implementation of CheckoutService.
 */
public class CheckoutServiceStub implements CheckoutService {
    @Override
    public CheckoutResult checkout(Cart cart, Customer customer, boolean isMember, PaymentMethod paymentMethod) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
