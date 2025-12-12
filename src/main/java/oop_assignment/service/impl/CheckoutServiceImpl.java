package oop_assignment.service.impl;

import oop_assignment.constant.SystemConstants;
import oop_assignment.exception.InvalidInputException;
import oop_assignment.exception.InsufficientBalanceException;
import oop_assignment.exception.OutOfStockException;
import oop_assignment.menu.PaymentMethod;
import oop_assignment.model.Cart;
import oop_assignment.model.CartItem;
import oop_assignment.model.CheckoutResult;
import oop_assignment.model.Customer;
import oop_assignment.model.PricingSummary;
import oop_assignment.service.*;

import java.util.Map;

/**
 * Implementation of CheckoutService.
 */
public class CheckoutServiceImpl implements CheckoutService {

    private final PricingService pricingService;
    private final InventoryService inventoryService;
    private final CustomerAccountService customerAccountService;
    private final CustomerService customerService;
    private final SalesService salesService;
    private final ReceiptService receiptService;
    private final QRCodeService qrCodeService;

    public CheckoutServiceImpl(PricingService pricingService, InventoryService inventoryService,
                               CustomerAccountService customerAccountService, CustomerService customerService,
                               SalesService salesService, ReceiptService receiptService, QRCodeService qrCodeService) {
        this.pricingService = pricingService;
        this.inventoryService = inventoryService;
        this.customerAccountService = customerAccountService;
        this.customerService = customerService;
        this.salesService = salesService;
        this.receiptService = receiptService;
        this.qrCodeService = qrCodeService;
    }

    @Override
    public CheckoutResult checkout(Cart cart, Customer customer, boolean isMember, PaymentMethod paymentMethod) {
        if (cart == null || cart.isEmpty()) {
            throw new InvalidInputException("Cart cannot be null or empty");
        }
        if (isMember && customer == null) {
            throw new InvalidInputException("Customer must be provided for member checkout");
        }

        // Check stock
        for (CartItem item : cart.getItems()) {
            if (!inventoryService.isEnoughStock(item.getItem(), item.getQuantity())) {
                throw new OutOfStockException("Not enough stock for " + item.getItem().getName());
            }
        }

        PricingSummary pricingSummary = pricingService.calculate(cart, isMember, customer);
        int points = (int) (pricingSummary.getGrandTotal() * SystemConstants.POINTS_PER_RINGGIT);

        if (paymentMethod == PaymentMethod.MEMBER_BALANCE) {
            if (!customerAccountService.hasEnoughBalance(customer, pricingSummary.getGrandTotal())) {
                throw new InsufficientBalanceException("Insufficient balance for payment");
            }
            customerAccountService.debitBalance(customer, pricingSummary.getGrandTotal());
            customerAccountService.addPoints(customer, points);
        } else if (paymentMethod == PaymentMethod.QR_PAYMENT) {
            qrCodeService.generatePaymentCode("Payment for " + pricingSummary.getGrandTotal());
            if (customer != null) {
                customerAccountService.addPoints(customer, points);
            }

        }

        inventoryService.decreaseStock(cart);
        salesService.recordSale(cart, pricingSummary, customer);
        if (customer != null) {
            customer.setDiscountAmount(0.0);
            if (customer.getAppliedVoucher() != null) {
                Map<String, Integer> vouchers = customer.getDiscountVouchers();
                int count = vouchers.get(customer.getAppliedVoucher());
                if (count > 1) {
                    vouchers.put(customer.getAppliedVoucher(), count - 1);
                } else {
                    vouchers.remove(customer.getAppliedVoucher());
                }
                customer.setAppliedVoucher(null);
            }
            customerService.updateCustomer(customer);
        }
        String receipt = receiptService.generateReceipt(cart, pricingSummary, customer);

        return new CheckoutResult(cart, pricingSummary, customer);
    }
}
