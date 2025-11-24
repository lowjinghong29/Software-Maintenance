package oop_assignment.service.impl;

import oop_assignment.model.Cart;
import oop_assignment.model.Customer;
import oop_assignment.model.PricingSummary;
import oop_assignment.service.ReceiptService;

/**
 * Stub implementation of ReceiptService.
 */
public class ReceiptServiceStub implements ReceiptService {
    @Override
    public String generateReceipt(Cart cart, PricingSummary pricingSummary, Customer customer) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
