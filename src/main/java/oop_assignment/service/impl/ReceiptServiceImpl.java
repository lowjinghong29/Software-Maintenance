package oop_assignment.service.impl;

import oop_assignment.constant.Messages;
import oop_assignment.model.Cart;
import oop_assignment.model.CartItem;
import oop_assignment.model.Customer;
import oop_assignment.model.PricingSummary;
import oop_assignment.service.ReceiptService;
import java.time.LocalDateTime;

/**
 * Implementation of ReceiptService.
 */
public class ReceiptServiceImpl implements ReceiptService {

    @Override
    public String generateReceipt(Cart cart, PricingSummary pricingSummary, Customer customer) {
        StringBuilder sb = new StringBuilder();
        sb.append(Messages.RECEIPT_HEADER);
        sb.append("Date/Time: ").append(LocalDateTime.now()).append("\n");
        if (customer != null) {
            sb.append("Member: ").append(customer.getName()).append(" (ID: ").append(customer.getEmail()).append(")\n");
        }
        sb.append("----------------------------------------\n");
        for (CartItem item : cart.getItems()) {
            sb.append(String.format("%s x %d = RM%.2f\n", item.getItem().getName(), item.getQuantity(), item.getLineTotal()));
        }
        sb.append("----------------------------------------\n");
        sb.append(String.format("Subtotal: RM%.2f\n", pricingSummary.getSubtotal()));
        sb.append(String.format("Tax (6%%): RM%.2f\n", pricingSummary.getTaxAmount()));
        sb.append(String.format("Delivery fee: RM%.2f\n", pricingSummary.getDeliveryFee()));
        if (pricingSummary.getDiscountAmount() > 0) {
            sb.append(String.format("Discount: -RM%.2f\n", pricingSummary.getDiscountAmount()));
        }
        sb.append(String.format("Grand Total: RM%.2f\n", pricingSummary.getGrandTotal()));
        if (customer != null) {
            sb.append(String.format("Remaining Balance: RM%.2f\n", customer.getBalance()));
        }
        return sb.toString();
    }
}
