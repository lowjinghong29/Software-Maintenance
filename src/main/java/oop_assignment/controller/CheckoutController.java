package oop_assignment.controller;

import oop_assignment.constant.Messages;
import oop_assignment.exception.InsufficientBalanceException;
import oop_assignment.exception.InvalidInputException;
import oop_assignment.exception.OutOfStockException;
import oop_assignment.exception.TrapstarException;
import oop_assignment.menu.PaymentMethod;
import oop_assignment.model.Cart;
import oop_assignment.model.CheckoutResult;
import oop_assignment.model.Customer;
import oop_assignment.model.Groceries;
import oop_assignment.repository.CartRepository;
import oop_assignment.service.CheckoutService;
import oop_assignment.service.InventoryService;
import oop_assignment.service.ReceiptService;
import oop_assignment.util.InputUtils;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Controller for handling checkout interactions.
 */
public class CheckoutController {
    private final Scanner scanner;
    private final InventoryService inventoryService;
    private final CheckoutService checkoutService;
    private final ReceiptService receiptService;
    private final CartRepository cartRepository;

    public CheckoutController(Scanner scanner, InventoryService inventoryService,
                              CheckoutService checkoutService, ReceiptService receiptService, CartRepository cartRepository) {
        if (scanner == null || inventoryService == null || checkoutService == null || receiptService == null || cartRepository == null) {
            throw new IllegalArgumentException("Dependencies cannot be null");
        }
        this.scanner = scanner;
        this.inventoryService = inventoryService;
        this.checkoutService = checkoutService;
        this.receiptService = receiptService;
        this.cartRepository = cartRepository;
    }

    public void startCheckout(Customer currentCustomer) {
        String userId = (currentCustomer != null) ? currentCustomer.getId() : "guest";
        Cart cart = cartRepository.loadCart(userId);

        while (true) {
            List<Groceries> groceries = inventoryService.getAllGroceries();
            System.out.println("Available Groceries:");
            for (int i = 0; i < groceries.size(); i++) {
                Groceries item = groceries.get(i);
                System.out.printf("  %d. %s - RM%.2f (Stock: %d)%n", i + 1, item.getName(), item.getPrice(), item.getStockQuantity());
            }
            System.out.println();

            int choice = InputUtils.readInt(scanner, "Select an item by entering its number (or 0 to finish): ");
            if (choice == 0) {
                if (cart.isEmpty()) {
                    System.out.println("Cart is empty. No checkout performed.");
                    return;
                }
                // Show cart summary and confirm
                System.out.println("Your Cart:");
                for (oop_assignment.model.CartItem item : cart.getItems()) {
                    System.out.printf("- %s x %d = RM%.2f%n", item.getItem().getName(), item.getQuantity(), item.getLineTotal());
                }
                System.out.printf("Subtotal: RM%.2f%n", cart.getSubtotal());
                System.out.print("Proceed to checkout? (Y/N): ");
                String confirm = scanner.nextLine().trim().toUpperCase();
                if (!confirm.equals("Y")) {
                    // Save the cart for later
                    cartRepository.saveCart(userId, cart);
                    System.out.println("Cart saved. You can view and checkout from the View Cart option.");
                    return;
                }
                // Proceed to checkout
                break;
            } else {
                int index = choice - 1;
                if (index < 0 || index >= groceries.size()) {
                    System.out.println("Invalid item number.");
                    continue;
                }

                Groceries selectedItem = groceries.get(index);
                int quantity = InputUtils.readInt(scanner, "Enter quantity: ");
                if (quantity <= 0) {
                    System.out.println("Quantity must be > 0.");
                    continue;
                }

                if (!inventoryService.isEnoughStock(selectedItem, quantity)) {
                    System.out.println("Not enough stock available for the selected item.");
                    continue;
                }

                cart.addItem(selectedItem, quantity);
                System.out.println("Item added to cart.");
                cartRepository.saveCart(userId, cart);
            }
        }

        // Voucher selection for members
        if (currentCustomer != null) {
            Map<String, Integer> vouchers = currentCustomer.getDiscountVouchers();
            if (!vouchers.isEmpty()) {
                System.out.println("\nAvailable Discount Vouchers:");
                List<String> voucherList = new java.util.ArrayList<>(vouchers.keySet());
                for (int i = 0; i < voucherList.size(); i++) {
                    String v = voucherList.get(i);
                    System.out.println((i + 1) + ". " + v + " (" + vouchers.get(v) + " available)");
                }
                System.out.println("0. No voucher");
                int voucherChoice = InputUtils.readInt(scanner, "Select a voucher to apply (or 0 for none): ");
                if (voucherChoice > 0 && voucherChoice <= voucherList.size()) {
                    String selectedVoucher = voucherList.get(voucherChoice - 1);
                    if (vouchers.get(selectedVoucher) > 0) {
                        double discount = Double.parseDouble(selectedVoucher.substring(2));
                        currentCustomer.setDiscountAmount(discount);
                        currentCustomer.setAppliedVoucher(selectedVoucher);
                        System.out.println("Applied " + selectedVoucher + " discount.");
                    } else {
                        System.out.println("No vouchers of that type available.");
                    }
                }
            }
        }

        // Proceed to payment and checkout
        boolean isMember = (currentCustomer != null);
        PaymentMethod paymentMethod;
        if (isMember) {
            System.out.println("Choose payment method:");
            System.out.println("1. Pay with member balance");
            System.out.println("2. Pay with QR payment");
            System.out.println("0. Cancel checkout");
            int paymentChoice = InputUtils.readInt(scanner, "Enter choice: ");
            if (paymentChoice == 0) {
                System.out.println("Checkout canceled.");
                return;
            } else if (paymentChoice == 1) {
                paymentMethod = PaymentMethod.MEMBER_BALANCE;
            } else if (paymentChoice == 2) {
                paymentMethod = PaymentMethod.QR_PAYMENT;
            } else {
                System.out.println("Checkout failed: Invalid payment method");
                return;
            }
        } else {
            paymentMethod = PaymentMethod.QR_PAYMENT;
            System.out.println("Non-member checkout: Payment via QR code.");
        }

        try {
            CheckoutResult result = checkoutService.checkout(cart, currentCustomer, isMember, paymentMethod);
            System.out.println("Checkout completed successfully!");
            String receipt = receiptService.generateReceipt(cart, result.getPricingSummary(), currentCustomer);
            System.out.println(receipt);
            // Notify points earned
            if (currentCustomer != null) {
                int pointsEarned = (int) result.getPricingSummary().getGrandTotal();
                System.out.println("You have earned " + pointsEarned + " points for this purchase!");
            }
            // Empty the cart after successful checkout
            cartRepository.saveCart(userId, new Cart());
        } catch (InvalidInputException | OutOfStockException | InsufficientBalanceException e) {
            System.out.println("Checkout failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Checkout failed: An unexpected error occurred.");
        }
    }

    public void checkoutExistingCart(Cart cart, Customer customer) {
        if (cart.isEmpty()) {
            System.out.println("Cart is empty. No checkout performed.");
            return;
        }

        // Proceed to payment and checkout
        boolean isMember = (customer != null);
        PaymentMethod paymentMethod;
        if (isMember) {
            System.out.println("Choose payment method:");
            System.out.println("1. Pay with member balance");
            System.out.println("2. Pay with QR payment");
            System.out.println("0. Cancel checkout");
            int paymentChoice = InputUtils.readInt(scanner, "Enter choice: ");
            if (paymentChoice == 0) {
                System.out.println("Checkout canceled.");
                return;
            } else if (paymentChoice == 1) {
                paymentMethod = PaymentMethod.MEMBER_BALANCE;
            } else if (paymentChoice == 2) {
                paymentMethod = PaymentMethod.QR_PAYMENT;
            } else {
                System.out.println("Checkout failed: Invalid payment method");
                return;
            }
        } else {
            paymentMethod = PaymentMethod.QR_PAYMENT;
            System.out.println("Non-member checkout: Payment via QR code.");
        }

        try {
            CheckoutResult result = checkoutService.checkout(cart, customer, isMember, paymentMethod);
            System.out.println("Checkout completed successfully!");
            String receipt = receiptService.generateReceipt(cart, result.getPricingSummary(), customer);
            System.out.println(receipt);
            // Notify points earned
            if (customer != null) {
                int pointsEarned = (int) result.getPricingSummary().getGrandTotal();
                System.out.println("You have earned " + pointsEarned + " points for this purchase!");
            }
            // Empty the cart after successful checkout
            String userId = (customer != null) ? customer.getId() : "guest";
            cartRepository.saveCart(userId, new Cart());
        } catch (InvalidInputException | OutOfStockException | InsufficientBalanceException e) {
            System.out.println("Checkout failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Checkout failed: An unexpected error occurred.");
        }
    }
}
