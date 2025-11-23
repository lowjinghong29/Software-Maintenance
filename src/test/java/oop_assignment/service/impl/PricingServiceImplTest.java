package oop_assignment.service.impl;

import oop_assignment.constant.SystemConstants;
import oop_assignment.model.Cart;
import oop_assignment.model.Groceries;
import oop_assignment.model.PricingSummary;
import oop_assignment.service.PricingService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PricingServiceImplTest {

    private final PricingService pricingService = new PricingServiceImpl();

    @Test
    void testCalculate_NonMember_WithItems() {
        Cart cart = new Cart();
        Groceries item1 = new Groceries("Apple", 5.00, 10);
        Groceries item2 = new Groceries("Banana", 10.00, 5);
        cart.addItem(item1, 2);
        cart.addItem(item2, 1);

        PricingSummary summary = pricingService.calculate(cart, false);

        assertEquals(20.00, summary.getSubtotal(), 0.01);
        assertEquals(20.00 * SystemConstants.TAX_RATE, summary.getTaxAmount(), 0.01);
        assertEquals(SystemConstants.DELIVERY_FEE_NON_MEMBER, summary.getDeliveryFee(), 0.01);
        assertEquals(0.0, summary.getDiscountAmount(), 0.01);
        double expectedGrand = 20.00 + (20.00 * SystemConstants.TAX_RATE) + SystemConstants.DELIVERY_FEE_NON_MEMBER;
        assertEquals(expectedGrand, summary.getGrandTotal(), 0.01);
    }

    @Test
    void testCalculate_Member_FreeDelivery() {
        Cart cart = new Cart();
        Groceries item = new Groceries("Apple", 10.00, 10);
        cart.addItem(item, 1);

        PricingSummary summary = pricingService.calculate(cart, true);

        assertEquals(10.00, summary.getSubtotal(), 0.01);
        assertEquals(SystemConstants.DELIVERY_FEE_MEMBER, summary.getDeliveryFee(), 0.01);
    }

    @Test
    void testCalculate_NullCart_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> pricingService.calculate(null, false));
    }
}
