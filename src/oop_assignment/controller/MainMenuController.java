package oop_assignment.controller;

import oop_assignment.menu.MainMenuOption;
import oop_assignment.model.Customer;
import oop_assignment.model.Session;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Controller for handling the main menu loop and delegating to feature handlers.
 */
public class MainMenuController {
    private final Scanner scanner;
    private final CheckoutController checkoutController;
    private final MembershipController membershipController;
    private final ReportController reportController;
    private final Session session;

    public MainMenuController(Scanner scanner, CheckoutController checkoutController,
                              MembershipController membershipController, ReportController reportController, Session session) {
        this.scanner = scanner;
        this.checkoutController = checkoutController;
        this.membershipController = membershipController;
        this.reportController = reportController;
        this.session = session;
    }

    public void start() {
        while (true) {
            System.out.println("=== Trapstar Groceries ===");
            System.out.println("Welcome to Trapstar Groceries!");
            if (session.hasCustomer()) {
                System.out.println("Logged in as: " + session.getCurrentCustomer().getName());
            }
            for (MainMenuOption option : MainMenuOption.values()) {
                System.out.println(option);
            }
            System.out.print("Enter option: ");
            try {
                int code = scanner.nextInt();
                MainMenuOption option = MainMenuOption.fromCode(code);
                switch (option) {
                    case PURCHASE:
                        Customer currentCustomer = session.getCurrentCustomer();
                        checkoutController.startCheckout(currentCustomer);
                        break;
                    case REDEMPTION:
                        handleRedemption();
                        break;
                    case MEMBERSHIP:
                        membershipController.start();
                        break;
                    case REPORTS:
                        reportController.start();
                        break;
                    case STAFF_MENU:
                        handleStaffMenu();
                        break;
                    case EXIT:
                        System.out.println("Thank you for using Trapstar Groceries. Goodbye!");
                        return;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid option, please try again.");
            } catch (InputMismatchException e) {
                System.out.println("Invalid input, please enter a number.");
                scanner.nextLine(); // consume invalid input
            }
        }
    }

    private void handleRedemption() {
        System.out.println("Redemption feature is not yet wired to the new architecture.");
    }

    private void handleStaffMenu() {
        System.out.println("Staff Menu feature is not yet wired to the new architecture.");
    }
}
