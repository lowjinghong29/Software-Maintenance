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
import oop_assignment.service.CheckoutService;
import oop_assignment.service.InventoryService;
import oop_assignment.service.ReceiptService;
import oop_assignment.util.InputUtils;
import java.util.List;
import java.util.Scanner;

/**
 * Controller for handling checkout interactions.
 */
public class CheckoutController {
    private final Scanner scanner;
    private final InventoryService inventoryService;
    private final CheckoutService checkoutService;
    private final ReceiptService receiptService;

    public CheckoutController(Scanner scanner, InventoryService inventoryService,
                              CheckoutService checkoutService, ReceiptService receiptService) {
        if (scanner == null || inventoryService == null || checkoutService == null || receiptService == null) {
            throw new IllegalArgumentException("Dependencies cannot be null");
        }
        this.scanner = scanner;
        this.inventoryService = inventoryService;
        this.checkoutService = checkoutService;
        this.receiptService = receiptService;
    }

    public void startCheckout(Customer currentCustomer) {
        Cart cart = new Cart();

        boolean addingItems = true;
        while (addingItems) {
            List<Groceries> groceries = inventoryService.getAllGroceries();
            System.out.println("Available groceries:");
            for (int i = 0; i < groceries.size(); i++) {
                Groceries item = groceries.get(i);
                System.out.printf("%d. %s - RM%.2f (Stock: %d)%n", i + 1, item.getName(), item.getPrice(), item.getStockQuantity());
            }

            int index = InputUtils.readInt(scanner, "Select item by number: ") - 1;
            if (index < 0 || index >= groceries.size()) {
                System.out.println(Messages.INVALID_OPTION);
                continue;
            }

            Groceries selectedItem = groceries.get(index);
            int quantity = InputUtils.readInt(scanner, "Enter quantity: ");
            if (quantity <= 0) {
                System.out.println("Quantity must be greater than 0.");
                continue;
            }

            if (!inventoryService.isEnoughStock(selectedItem, quantity)) {
                System.out.println(Messages.OUT_OF_STOCK);
                continue;
            }

            cart.addItem(selectedItem, quantity);

            String addAnother = InputUtils.readNonEmptyString(scanner, "Add another item? (Y/N): ");
            if (!addAnother.equalsIgnoreCase("Y")) {
                addingItems = false;
            }
        }

        if (cart.isEmpty()) {
            System.out.println(Messages.CART_EMPTY);
            return;
        }

        boolean isMember = (currentCustomer != null);
        PaymentMethod paymentMethod;
        if (isMember) {
            System.out.println("Choose payment method:");
            System.out.println("1. Pay with member balance");
            System.out.println("2. Pay with QR payment");
            int choice = InputUtils.readInt(scanner, "Enter choice: ");
            paymentMethod = (choice == 1) ? PaymentMethod.MEMBER_BALANCE : PaymentMethod.QR_PAYMENT;
        } else {
            paymentMethod = PaymentMethod.QR_PAYMENT;
        }

        try {
            CheckoutResult result = checkoutService.checkout(cart, currentCustomer, isMember, paymentMethod);
            System.out.println(Messages.CHECKOUT_SUCCESS);
            String receipt = receiptService.generateReceipt(cart, result.getPricingSummary(), currentCustomer);
            System.out.println(receipt);
        } catch (InvalidInputException | OutOfStockException | InsufficientBalanceException e) {
            System.out.println(e.getMessage());
        } catch (TrapstarException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
