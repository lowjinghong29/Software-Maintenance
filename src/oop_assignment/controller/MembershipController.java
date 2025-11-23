package oop_assignment.controller;

import oop_assignment.constant.Messages;
import oop_assignment.exception.AuthenticationFailedException;
import oop_assignment.exception.InvalidInputException;
import oop_assignment.menu.MembershipMenuOption;
import oop_assignment.model.Customer;
import oop_assignment.model.Session;
import oop_assignment.service.AuthService;
import oop_assignment.service.CustomerAccountService;
import oop_assignment.service.CustomerService;
import oop_assignment.util.InputUtils;
import java.util.Scanner;

/**
 * Controller for handling membership interactions.
 */
public class MembershipController {
    private final Scanner scanner;
    private final Session session;
    private final AuthService authService;
    private final CustomerService customerService;
    private final CustomerAccountService customerAccountService;

    public MembershipController(Scanner scanner, Session session, AuthService authService,
                                CustomerService customerService, CustomerAccountService customerAccountService) {
        if (scanner == null || session == null || authService == null ||
            customerService == null || customerAccountService == null) {
            throw new IllegalArgumentException("Dependencies cannot be null");
        }
        this.scanner = scanner;
        this.session = session;
        this.authService = authService;
        this.customerService = customerService;
        this.customerAccountService = customerAccountService;
    }

    public void start() {
        while (true) {
            System.out.println("=== Membership Menu ===");
            for (MembershipMenuOption option : MembershipMenuOption.values()) {
                System.out.println(option);
            }
            int code = InputUtils.readInt(scanner, "Enter option: ");
            try {
                MembershipMenuOption option = MembershipMenuOption.fromCode(code);
                switch (option) {
                    case LOGIN:
                        handleLogin();
                        break;
                    case LOGOUT:
                        handleLogout();
                        break;
                    case VIEW_BALANCE:
                        handleViewBalance();
                        break;
                    case TOP_UP:
                        handleTopUp();
                        break;
                    case BACK:
                        return;
                }
            } catch (IllegalArgumentException e) {
                System.out.println(Messages.INVALID_OPTION);
            }
        }
    }

    private void handleLogin() {
        if (session.hasCustomer()) {
            System.out.println("You are already logged in as " + session.getCurrentCustomer().getName() + ". Please log out first.");
            return;
        }
        String idOrEmail = InputUtils.readNonEmptyString(scanner, "Enter member ID or email: ");
        String password = InputUtils.readNonEmptyString(scanner, "Enter password: ");
        try {
            Customer customer = authService.customerLogin(idOrEmail, password);
            session.setCurrentCustomer(customer);
            System.out.println("Login successful. Welcome, " + customer.getName() + "!");
        } catch (AuthenticationFailedException e) {
            System.out.println("Invalid credentials. Please try again.");
        }
    }

    private void handleLogout() {
        if (!session.hasCustomer()) {
            System.out.println("No member is currently logged in.");
        } else {
            System.out.println("Goodbye, " + session.getCurrentCustomer().getName() + ".");
            session.clearCustomer();
        }
    }

    private void handleViewBalance() {
        if (!session.hasCustomer()) {
            System.out.println("Please log in as a member to view your wallet and points.");
            return;
        }
        Customer customer = session.getCurrentCustomer();
        System.out.println("Member: " + customer.getName());
        System.out.println("Wallet balance: RM" + String.format("%.2f", customer.getBalance()));
        System.out.println("Points: " + customer.getLoyaltyPoints());
    }

    private void handleTopUp() {
        if (!session.hasCustomer()) {
            System.out.println("Please log in as a member to top up your wallet.");
            return;
        }
        double amount = InputUtils.readDouble(scanner, "Enter top-up amount (RM): ");
        if (amount <= 0) {
            System.out.println("Amount must be greater than 0.");
            return;
        }
        try {
            customerAccountService.addBalance(session.getCurrentCustomer(), amount);
            System.out.println("Top-up successful. New balance: RM" + String.format("%.2f", session.getCurrentCustomer().getBalance()));
        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
        }
    }
}
