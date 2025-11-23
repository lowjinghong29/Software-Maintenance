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
import oop_assignment.repository.CartRepository;
import oop_assignment.model.Cart;

/**
 * Controller for handling membership interactions.
 */
public class MembershipController {
    private final Scanner scanner;
    private final Session session;
    private final AuthService authService;
    private final CustomerService customerService;
    private final CustomerAccountService customerAccountService;
    private final CartRepository cartRepository;

    public MembershipController(Scanner scanner, Session session, AuthService authService,
                                CustomerService customerService, CustomerAccountService customerAccountService, CartRepository cartRepository) {
        if (scanner == null || session == null || authService == null ||
            customerService == null || customerAccountService == null || cartRepository == null) {
            throw new IllegalArgumentException("Dependencies cannot be null");
        }
        this.scanner = scanner;
        this.session = session;
        this.authService = authService;
        this.customerService = customerService;
        this.customerAccountService = customerAccountService;
        this.cartRepository = cartRepository;
    }

    public void start() {
        while (true) {
            System.out.println(Messages.MEMBERSHIP_MENU_HEADER);
            for (MembershipMenuOption option : MembershipMenuOption.values()) {
                System.out.println(option);
            }
            int code = InputUtils.readInt(scanner, Messages.MAIN_MENU_PROMPT);
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
            System.out.println(String.format(Messages.ALREADY_LOGGED_IN, session.getCurrentCustomer().getName()));
            return;
        }
        String idOrEmail = InputUtils.readNonEmptyString(scanner, Messages.LOGIN_PROMPT_ID);
        String password = InputUtils.readNonEmptyString(scanner, Messages.LOGIN_PROMPT_PASSWORD);
        try {
            Customer customer = authService.customerLogin(idOrEmail, password);
            session.setCurrentCustomer(customer);
            // Load user's cart
            Cart userCart = cartRepository.loadCart(customer.getId());
            session.setCurrentCart(userCart);
            System.out.println(Messages.LOGIN_SUCCESS + customer.getName() + "!");
        } catch (AuthenticationFailedException e) {
            System.out.println(Messages.LOGIN_FAILED);
        }
    }

    private void handleLogout() {
        if (!session.hasCustomer()) {
            System.out.println(Messages.NO_MEMBER_LOGGED_IN);
        } else {
            // Save user's cart
            cartRepository.saveCart(session.getCurrentCustomer().getId(), session.getCurrentCart());
            System.out.println(String.format(Messages.GOODBYE_MEMBER, session.getCurrentCustomer().getName()));
            session.clearCustomer();
            session.setCurrentCart(new Cart()); // reset to empty
        }
    }

    private void handleViewBalance() {
        if (!session.hasCustomer()) {
            System.out.println(Messages.LOGIN_REQUIRED_VIEW);
            return;
        }
        Customer customer = session.getCurrentCustomer();
        System.out.println(String.format(Messages.MEMBER_INFO, customer.getName()));
        System.out.println(Messages.BALANCE_DISPLAY + String.format("%.2f", customer.getBalance()));
        System.out.println(Messages.POINTS_DISPLAY + customer.getLoyaltyPoints());
    }

    private void handleTopUp() {
        if (!session.hasCustomer()) {
            System.out.println(Messages.LOGIN_REQUIRED_TOPUP);
            return;
        }
        double amount = InputUtils.readDouble(scanner, Messages.TOPUP_PROMPT);
        if (amount <= 0) {
            System.out.println(Messages.AMOUNT_INVALID);
            return;
        }
        try {
            customerAccountService.addBalance(session.getCurrentCustomer(), amount);
            System.out.println(Messages.TOPUP_SUCCESS + String.format("%.2f", session.getCurrentCustomer().getBalance()));
        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
        }
    }
}
