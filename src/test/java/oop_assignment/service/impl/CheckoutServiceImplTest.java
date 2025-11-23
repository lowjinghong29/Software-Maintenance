package oop_assignment.service.impl;

import oop_assignment.exception.InvalidInputException;
import oop_assignment.exception.InsufficientBalanceException;
import oop_assignment.menu.PaymentMethod;
import oop_assignment.model.*;
import oop_assignment.service.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CheckoutServiceImplTest {

    @Test
    void testCheckout_Guest_QRPayment_Success() {
        Cart cart = new Cart();
        Groceries item = new Groceries("Apple", 10.00, 10);
        cart.addItem(item, 1);

        StubPricingService pricingService = new StubPricingService();
        StubInventoryService inventoryService = new StubInventoryService();
        StubCustomerAccountService customerAccountService = new StubCustomerAccountService();
        StubSalesService salesService = new StubSalesService();
        StubReceiptService receiptService = new StubReceiptService();
        StubQRCodeService qrCodeService = new StubQRCodeService();

        CheckoutService checkoutService = new CheckoutServiceImpl(pricingService, inventoryService, customerAccountService, salesService, receiptService, qrCodeService);

        CheckoutResult result = checkoutService.checkout(cart, null, false, PaymentMethod.QR_PAYMENT);

        assertNotNull(result);
        assertEquals(cart, result.getCart());
        assertTrue(inventoryService.decreaseStockCalled);
        assertTrue(salesService.recordSaleCalled);
        assertTrue(qrCodeService.generateCalled);
    }

    @Test
    void testCheckout_Member_BalancePayment_SufficientBalance_Success() {
        Cart cart = new Cart();
        Groceries item = new Groceries("Apple", 10.00, 10);
        cart.addItem(item, 1);
        Customer customer = new Customer("John", "pass", "john@example.com", "123", "Addr", 100, 50.00);

        StubPricingService pricingService = new StubPricingService();
        StubInventoryService inventoryService = new StubInventoryService();
        StubCustomerAccountService customerAccountService = new StubCustomerAccountService();
        customerAccountService.hasEnoughBalanceResult = true;
        StubSalesService salesService = new StubSalesService();
        StubReceiptService receiptService = new StubReceiptService();
        StubQRCodeService qrCodeService = new StubQRCodeService();

        CheckoutService checkoutService = new CheckoutServiceImpl(pricingService, inventoryService, customerAccountService, salesService, receiptService, qrCodeService);

        CheckoutResult result = checkoutService.checkout(cart, customer, true, PaymentMethod.MEMBER_BALANCE);

        assertNotNull(result);
        assertEquals(40.00, customerAccountService.debitCalledAmount, 0.01);
        assertEquals(4, customerAccountService.pointsAdded);
    }

    @Test
    void testCheckout_Member_BalancePayment_InsufficientBalance_Throws() {
        Cart cart = new Cart();
        Groceries item = new Groceries("Apple", 10.00, 10);
        cart.addItem(item, 1);
        Customer customer = new Customer("John", "pass", "john@example.com", "123", "Addr", 100, 10.00);

        StubPricingService pricingService = new StubPricingService();
        StubInventoryService inventoryService = new StubInventoryService();
        StubCustomerAccountService customerAccountService = new StubCustomerAccountService();
        customerAccountService.hasEnoughBalanceResult = false;
        StubSalesService salesService = new StubSalesService();
        StubReceiptService receiptService = new StubReceiptService();
        StubQRCodeService qrCodeService = new StubQRCodeService();

        CheckoutService checkoutService = new CheckoutServiceImpl(pricingService, inventoryService, customerAccountService, salesService, receiptService, qrCodeService);

        assertThrows(InsufficientBalanceException.class, () -> checkoutService.checkout(cart, customer, true, PaymentMethod.MEMBER_BALANCE));
        assertFalse(inventoryService.decreaseStockCalled);
        assertFalse(salesService.recordSaleCalled);
    }

    @Test
    void testCheckout_EmptyCart_ThrowsInvalidInputException() {
        Cart cart = new Cart();

        StubPricingService pricingService = new StubPricingService();
        StubInventoryService inventoryService = new StubInventoryService();
        StubCustomerAccountService customerAccountService = new StubCustomerAccountService();
        StubSalesService salesService = new StubSalesService();
        StubReceiptService receiptService = new StubReceiptService();
        StubQRCodeService qrCodeService = new StubQRCodeService();

        CheckoutService checkoutService = new CheckoutServiceImpl(pricingService, inventoryService, customerAccountService, salesService, receiptService, qrCodeService);

        assertThrows(InvalidInputException.class, () -> checkoutService.checkout(cart, null, false, PaymentMethod.QR_PAYMENT));
    }

    @Test
    void testCheckout_MemberButNullCustomer_ThrowsInvalidInputException() {
        Cart cart = new Cart();
        Groceries item = new Groceries("Apple", 10.00, 10);
        cart.addItem(item, 1);

        StubPricingService pricingService = new StubPricingService();
        StubInventoryService inventoryService = new StubInventoryService();
        StubCustomerAccountService customerAccountService = new StubCustomerAccountService();
        StubSalesService salesService = new StubSalesService();
        StubReceiptService receiptService = new StubReceiptService();
        StubQRCodeService qrCodeService = new StubQRCodeService();

        CheckoutService checkoutService = new CheckoutServiceImpl(pricingService, inventoryService, customerAccountService, salesService, receiptService, qrCodeService);

        assertThrows(InvalidInputException.class, () -> checkoutService.checkout(cart, null, true, PaymentMethod.MEMBER_BALANCE));
    }

    // Stub classes
    private static class StubPricingService implements PricingService {
        @Override
        public PricingSummary calculate(Cart cart, boolean isMember) {
            double subtotal = cart.getSubtotal();
            double tax = subtotal * 0.06;
            double delivery = isMember ? 0.0 : 4.90;
            return new PricingSummary(subtotal, tax, delivery, 0.0, subtotal + tax + delivery);
        }
    }

    private static class StubInventoryService implements InventoryService {
        boolean decreaseStockCalled = false;

        @Override
        public List<Groceries> getAllGroceries() {
            return List.of();
        }

        @Override
        public Groceries findById(String id) {
            return null;
        }

        @Override
        public boolean isEnoughStock(Groceries item, int quantity) {
            return true;
        }

        @Override
        public void decreaseStock(Cart cart) {
            decreaseStockCalled = true;
        }
    }

    private static class StubCustomerAccountService implements CustomerAccountService {
        boolean hasEnoughBalanceResult = true;
        double debitCalledAmount = 0;
        int pointsAdded = 0;

        @Override
        public boolean hasEnoughBalance(Customer customer, double amount) {
            return hasEnoughBalanceResult;
        }

        @Override
        public void debitBalance(Customer customer, double amount) {
            debitCalledAmount = amount;
        }

        @Override
        public void addBalance(Customer customer, double amount) {
        }

        @Override
        public void addPoints(Customer customer, int points) {
            pointsAdded = points;
        }
    }

    private static class StubSalesService implements SalesService {
        boolean recordSaleCalled = false;

        @Override
        public void recordSale(Cart cart, PricingSummary pricingSummary, Customer customer) {
            recordSaleCalled = true;
        }

        @Override
        public double getTotalRevenue() {
            return 0;
        }

        @Override
        public Map<String, Integer> getTotalQuantityByProductId() {
            return Map.of();
        }
    }

    private static class StubReceiptService implements ReceiptService {
        @Override
        public String generateReceipt(Cart cart, PricingSummary pricingSummary, Customer customer) {
            return "TEST_RECEIPT";
        }
    }

    private static class StubQRCodeService implements QRCodeService {
        boolean generateCalled = false;

        @Override
        public void generatePaymentCode(String content) {
            generateCalled = true;
        }
    }
}
