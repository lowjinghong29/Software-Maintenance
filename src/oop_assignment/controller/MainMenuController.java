package oop_assignment.controller;

import oop_assignment.constant.Messages;
import oop_assignment.menu.MainMenuOption;
import oop_assignment.model.Customer;
import oop_assignment.model.Groceries;
import oop_assignment.model.Session;
import oop_assignment.model.Staff;
import oop_assignment.service.AuthService;
import oop_assignment.service.CustomerService;
import oop_assignment.service.InventoryService;
import oop_assignment.util.InputUtils;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import oop_assignment.repository.CartRepository;
import oop_assignment.model.Cart;
import oop_assignment.model.CartItem;

/**
 * Controller for handling the main menu loop and delegating to feature handlers.
 */
public class MainMenuController {
    private final Scanner scanner;
    private final CheckoutController checkoutController;
    private final MembershipController membershipController;
    private final ReportController reportController;
    private final Session session;
    private final AuthService authService;
    private final CustomerService customerService;
    private final InventoryService inventoryService;
    private final CartRepository cartRepository;

    private static final String WELCOME_ART = "___________                             __                   ________                                .__               \n" +
"\\__    ___/___________  ______  _______/  |______ _______   /  _____/______  ____   ____  ___________|__| ____   ______\n" +
"  |    |  \\_  __ \\__  \\ \\____ \\/  ___/\\   __\\__  \\\\_  __ \\ /   \\  __\\_  __ \\/  _ \\_/ ___\\/ __ \\_  __ \\  |/ __ \\ /  ___/\n" +
"  |    |   |  | \\// __ \\|  |_> >___ \\  |  |  / __ \\|  | \\/ \\    \\_\\  \\  | \\(  <_> )  \\__\\  ___/|  | \\/  \\  ___/ \\___ \\ \n" +
"  |____|   |__|  (____  /   __/____  > |__| (____  /__|     \\______  /__|   \\____/ \\___  >___  >__|  |__|\\___  >____  >\n" +
"                      \\/|__|       \\/            \\/                \\/                  \\/    \\/              \\/     \\/\n";

    public MainMenuController(Scanner scanner, CheckoutController checkoutController,
                              MembershipController membershipController, ReportController reportController, Session session, AuthService authService, CustomerService customerService, InventoryService inventoryService, CartRepository cartRepository) {
        this.scanner = scanner;
        this.checkoutController = checkoutController;
        this.membershipController = membershipController;
        this.reportController = reportController;
        this.session = session;
        this.authService = authService;
        this.customerService = customerService;
        this.inventoryService = inventoryService;
        this.cartRepository = cartRepository;
    }

    public void start() {
        System.out.println(WELCOME_ART);
        System.out.println(Messages.WELCOME_MESSAGE);
        while (true) {
            System.out.println(Messages.APP_TITLE);
            if (session.hasCustomer()) {
                System.out.println(Messages.LOGGED_IN_AS + session.getCurrentCustomer().getName());
            }
            System.out.println("\n=== Main Menu ===");
            for (MainMenuOption option : MainMenuOption.values()) {
                System.out.println(option);
            }
            System.out.print(Messages.MAIN_MENU_PROMPT);
            try {
                int code = scanner.nextInt();
                scanner.nextLine(); // consume the newline
                MainMenuOption option = MainMenuOption.fromCode(code);
                switch (option) {
                    case STAFF:
                        handleStaffMenu();
                        break;
                    case CUSTOMER:
                        handleCustomer();
                        break;
                    case VIEW_CART:
                        handleViewCart();
                        break;
                    case EXIT:
                        System.out.println(Messages.GOODBYE);
                        return;
                }
            } catch (IllegalArgumentException e) {
                System.out.println(Messages.INVALID_OPTION);
            } catch (InputMismatchException e) {
                System.out.println(Messages.INVALID_NUMBER);
                scanner.nextLine(); // consume invalid input
            } catch (java.util.NoSuchElementException e) {
                System.out.println("\nNo input available. Exiting gracefully...");
                return;
            }
        }
    }

    private void handleRedemption() {
        if (!session.hasCustomer()) {
            System.out.println("\nPlease log in as a member to redeem points.");
            return;
        }
        Customer customer = session.getCurrentCustomer();
        System.out.println("\n=== Redemption ===");
        System.out.println("Your current points: " + customer.getLoyaltyPoints());
        if (customer.getLoyaltyPoints() < 100) {
            System.out.println("You need at least 100 points to redeem.");
            return;
        }
        System.out.println("Redeem 100 points for RM5 discount on next purchase? (Y/N)");
        String confirm = scanner.nextLine().trim().toUpperCase();
        if (confirm.equals("Y")) {
            customer.setLoyaltyPoints(customer.getLoyaltyPoints() - 100);
            try {
                customerService.updateCustomer(customer);
                System.out.println("Redemption successful! You have redeemed RM5 discount.");
                System.out.println("Show this message at checkout for discount.");
            } catch (Exception e) {
                System.out.println("Failed to redeem: " + e.getMessage());
            }
        } else {
            System.out.println("Redemption cancelled.");
        }
    }

    private void handleStaffMenu() {
        System.out.println(Messages.STAFF_LOGIN_HEADER);
        System.out.print(Messages.STAFF_LOGIN_USERNAME);
        String username = scanner.next();
        System.out.print(Messages.STAFF_LOGIN_PASSWORD);
        String password = scanner.next();
        try {
            Staff staff = authService.staffLogin(username, password);
            session.setCurrentStaff(staff);
            System.out.println("\nStaff login successful!\nWelcome, " + staff.getName() + "!");
            showStaffMenu();
        } catch (Exception e) {
            System.out.println("\nInvalid staff credentials. Access denied. Please try again.");
        }
    }

    private void showStaffMenu() {
        while (true) {
            System.out.println(Messages.STAFF_MENU_HEADER);
            System.out.println("1. Modify Customer Details");
            System.out.println("2. Add/Remove Groceries");
            System.out.println("3. Add Stock");
            System.out.println("4. Generate Report");
            System.out.println("5. Sort Groceries");
            System.out.println("0. Logout");
            System.out.print(Messages.MAIN_MENU_PROMPT);
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1:
                        handleModifyCustomer();
                        break;
                    case 2:
                        handleAddRemoveGroceries();
                        break;
                    case 3:
                        handleAddStock();
                        break;
                    case 4:
                        reportController.start();
                        break;
                    case 5:
                        handleSortGroceries();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println(Messages.INVALID_OPTION);
                }
            } catch (InputMismatchException e) {
                System.out.println(Messages.INVALID_NUMBER);
                scanner.nextLine();
            }
        }
    }

    private void handleCustomer() {
        while (true) {
            if (session.hasCustomer()) {
                // Logged in menu
                System.out.println("\n=== Member Services ===");
                System.out.println("Welcome back, " + session.getCurrentCustomer().getName() + "!");
                System.out.println("1. Add to Cart");
                System.out.println("2. Redemption");
                System.out.println("3. Top up Wallet");
                System.out.println("4. Logout");
                System.out.println("0. Back to Main Menu");
                System.out.print(Messages.MAIN_MENU_PROMPT);
                try {
                    int choice = scanner.nextInt();
                    scanner.nextLine(); // consume newline
                    switch (choice) {
                        case 1:
                            handleAddToCart(session.getCurrentCustomer());
                            break;
                        case 2:
                            handleRedemption();
                            break;
                        case 3:
                            membershipController.start(); // assuming top up is in membership
                            break;
                        case 4:
                            session.clearCustomer();
                            System.out.println(Messages.LOGOUT_MESSAGE);
                            break;
                        case 0:
                            return;
                        default:
                            System.out.println(Messages.INVALID_OPTION);
                    }
                } catch (InputMismatchException e) {
                    System.out.println(Messages.INVALID_NUMBER);
                    scanner.nextLine();
                }
            } else {
                // Not logged in menu
                System.out.println("\n=== Customer Services ===");
                System.out.println("1. Login as Member");
                System.out.println("2. Sign up Member");
                System.out.println("3. Add to Cart (Guest)");
                System.out.println("0. Back to Main Menu");
                System.out.print(Messages.MAIN_MENU_PROMPT);
                try {
                    int choice = scanner.nextInt();
                    scanner.nextLine(); // consume newline
                    switch (choice) {
                        case 1:
                            membershipController.start();
                            break;
                        case 2:
                            handleSignUp();
                            break;
                        case 3:
                            handleAddToCart(null); // Pass null customer for guest cart
                            break;
                        case 0:
                            return;
                        default:
                            System.out.println(Messages.INVALID_OPTION);
                    }
                } catch (InputMismatchException e) {
                    System.out.println(Messages.INVALID_NUMBER);
                    scanner.nextLine();
                }
            }
        }
    }

    private void handleModifyCustomer() {
        List<Customer> customers = customerService.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("\nNo customers available to modify.");
            return;
        }
        System.out.println("\nSelect a customer to modify:");
        for (int i = 0; i < customers.size(); i++) {
            Customer c = customers.get(i);
            System.out.printf("%d. %s (%s)\n", i + 1, c.getName(), c.getEmail());
        }
        int choice = InputUtils.readInt(scanner, "\nEnter customer number (or 0 to cancel): ") - 1;
        if (choice == -1) {
            return; // cancel
        }
        if (choice < 0 || choice >= customers.size()) {
            System.out.println("Invalid customer number. Please enter a number between 1 and " + customers.size() + " (or 0 to cancel).");
            return;
        }
        Customer selected = customers.get(choice);
        System.out.println("\nModifying customer: " + selected.getName());
        System.out.print("Enter new name (current: " + selected.getName() + "): ");
        String newName = scanner.nextLine().trim();
        if (!newName.isEmpty()) {
            selected.setName(newName);
        }
        System.out.print("Enter new email (current: " + selected.getEmail() + "): ");
        String newEmail = scanner.nextLine().trim();
        if (!newEmail.isEmpty()) {
            selected.setEmail(newEmail);
        }
        System.out.print("Enter new phone number (current: " + selected.getPhoneNumber() + "): ");
        String newPhone = scanner.nextLine().trim();
        if (!newPhone.isEmpty()) {
            selected.setPhoneNumber(newPhone);
        }
        System.out.print("Enter new mailing address (current: " + selected.getMailingAddress() + "): ");
        String newAddress = scanner.nextLine().trim();
        if (!newAddress.isEmpty()) {
            selected.setMailingAddress(newAddress);
        }
        try {
            customerService.updateCustomer(selected);
            System.out.println("\nCustomer details updated successfully!");
        } catch (Exception e) {
            System.out.println("\nFailed to update customer: " + e.getMessage());
        }
    }

    private void handleAddRemoveGroceries() {
        while (true) {
            System.out.println("\n=== Add/Remove Groceries ===");
            System.out.println("1. Add Grocery");
            System.out.println("2. Remove Grocery");
            System.out.println("0. Back");
            int choice = InputUtils.readInt(scanner, Messages.MAIN_MENU_PROMPT);
            switch (choice) {
                case 1:
                    handleAddGrocery();
                    break;
                case 2:
                    handleRemoveGrocery();
                    break;
                case 0:
                    return;
                default:
                    System.out.println(Messages.INVALID_OPTION);
            }
        }
    }

    private void handleAddGrocery() {
        System.out.print("Enter grocery name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }
        double price = InputUtils.readDouble(scanner, "Enter price: ");
        if (price <= 0) {
            System.out.println("Price must be > 0.");
            return;
        }
        int stock = InputUtils.readInt(scanner, "Enter initial stock: ");
        if (stock < 0) {
            System.out.println("Stock cannot be negative.");
            return;
        }
        Groceries grocery = new Groceries(name, price, stock);
        try {
            inventoryService.addGrocery(grocery);
            System.out.println("Grocery added successfully!");
        } catch (Exception e) {
            System.out.println("Failed to add grocery: " + e.getMessage());
        }
    }

    private void handleRemoveGrocery() {
        List<Groceries> groceries = inventoryService.getAllGroceries();
        if (groceries.isEmpty()) {
            System.out.println("No groceries to remove.");
            return;
        }
        System.out.println("Select grocery to remove:");
        for (int i = 0; i < groceries.size(); i++) {
            Groceries g = groceries.get(i);
            System.out.printf("%d. %s\n", i + 1, g.getName());
        }
        int choice = InputUtils.readInt(scanner, "Enter grocery number (or 0 to cancel): ") - 1;
        if (choice == -1) {
            return;
        }
        if (choice < 0 || choice >= groceries.size()) {
            System.out.println("Invalid grocery number. Please enter a number between 1 and " + groceries.size() + " (or 0 to cancel).");
            return;
        }
        String name = groceries.get(choice).getName();
        try {
            inventoryService.removeGrocery(name);
            System.out.println("Grocery removed successfully!");
        } catch (Exception e) {
            System.out.println("Failed to remove grocery: " + e.getMessage());
        }
    }

    private void handleAddStock() {
        List<Groceries> groceries = inventoryService.getAllGroceries();
        if (groceries.isEmpty()) {
            System.out.println("No groceries to add stock to.");
            return;
        }
        System.out.println("Select grocery to add stock:");
        for (int i = 0; i < groceries.size(); i++) {
            Groceries g = groceries.get(i);
            System.out.printf("%d. %s (Current stock: %d)\n", i + 1, g.getName(), g.getStockQuantity());
        }
        int choice = InputUtils.readInt(scanner, "Enter grocery number (or 0 to cancel): ") - 1;
        if (choice == -1) {
            return;
        }
        if (choice < 0 || choice >= groceries.size()) {
            System.out.println("Invalid grocery number. Please enter a number between 1 and " + groceries.size() + " (or 0 to cancel).");
            return;
        }
        String name = groceries.get(choice).getName();
        int amount = InputUtils.readInt(scanner, "Enter amount to add: ");
        if (amount <= 0) {
            System.out.println("Amount must be > 0.");
            return;
        }
        try {
            inventoryService.increaseStock(name, amount);
            System.out.println("Stock added successfully!");
        } catch (Exception e) {
            System.out.println("Failed to add stock: " + e.getMessage());
        }
    }

    private void handleSignUp() {
        System.out.println("\n=== Member Sign Up ===");
        System.out.print("Enter your name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }
        System.out.print("Enter your email: ");
        String email = scanner.nextLine().trim();
        if (email.isEmpty()) {
            System.out.println("Email cannot be empty.");
            return;
        }
        System.out.print("Enter your password: ");
        String password = scanner.nextLine().trim();
        if (password.isEmpty()) {
            System.out.println("Password cannot be empty.");
            return;
        }
        System.out.print("Enter your phone number: ");
        String phone = scanner.nextLine().trim();
        System.out.print("Enter your mailing address: ");
        String address = scanner.nextLine().trim();
        try {
            Customer newCustomer = new Customer("temp", name, password, email, phone, address, 0, 0.0);
            Customer registered = customerService.registerCustomer(newCustomer);
            System.out.println("\nSign up successful! Welcome, " + registered.getName() + "!");
            System.out.println("You can now log in as a member.");
        } catch (Exception e) {
            System.out.println("Sign up failed: " + e.getMessage());
        }
    }

    private void handleSortGroceries() {
        List<Groceries> groceries = inventoryService.getAllGroceries();
        if (groceries.isEmpty()) {
            System.out.println("No groceries to sort.");
            return;
        }
        System.out.println("Sort groceries by:");
        System.out.println("1. Name (A-Z)");
        System.out.println("2. Price (Low to High)");
        System.out.println("0. Cancel");
        int sortChoice = InputUtils.readInt(scanner, "Enter choice: ");
        switch (sortChoice) {
            case 1:
                groceries.sort((g1, g2) -> g1.getName().compareToIgnoreCase(g2.getName()));
                System.out.println("\nGroceries sorted by name:");
                break;
            case 2:
                groceries.sort((g1, g2) -> Double.compare(g1.getPrice(), g2.getPrice()));
                System.out.println("\nGroceries sorted by price:");
                break;
            case 0:
                return;
            default:
                System.out.println("Invalid choice.");
                return;
        }
        for (int i = 0; i < groceries.size(); i++) {
            Groceries g = groceries.get(i);
            System.out.printf("%d. %s - RM%.2f (Stock: %d)\n", i + 1, g.getName(), g.getPrice(), g.getStockQuantity());
        }
    }

    private void handleViewCart() {
        Cart cart;
        String userId;
        if (session.hasCustomer()) {
            userId = session.getCurrentCustomer().getId();
            cart = session.getCurrentCart();
        } else {
            userId = "guest";
            cart = cartRepository.loadCart(userId);
        }
        System.out.println("\n=== Your Cart ===");
        List<CartItem> cartItems = cart.getItems();
        if (cartItems.isEmpty()) {
            System.out.println("Your cart is empty.");
        } else {
            for (CartItem item : cartItems) {
                System.out.printf("- %s (RM%.2f) [Qty: %d]\n", item.getItem().getName(), item.getItem().getPrice(), item.getQuantity());
            }
            System.out.printf("Total Quantity: %d\n", cart.getTotalQuantity());
            System.out.printf("Subtotal: RM%.2f\n", cart.getSubtotal());
        }
        System.out.println("===================");
        System.out.println("1. Checkout");
        System.out.println("2. Continue Shopping");
        System.out.print(Messages.MAIN_MENU_PROMPT);
        try {
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            switch (choice) {
                case 1:
                    Customer customer = session.hasCustomer() ? session.getCurrentCustomer() : null;
                    checkoutController.checkoutExistingCart(cart, customer);
                    break;
                case 2:
                    // Do nothing, just return to menu
                    break;
                default:
                    System.out.println(Messages.INVALID_OPTION);
            }
        } catch (InputMismatchException e) {
            System.out.println(Messages.INVALID_NUMBER);
            scanner.nextLine();
        }
    }

    private void handleAddToCart(Customer customer) {
        List<Groceries> groceries = inventoryService.getAllGroceries();
        if (groceries.isEmpty()) {
            System.out.println("No groceries available.");
            return;
        }
        System.out.println("Select a grocery to add to cart:");
        for (int i = 0; i < groceries.size(); i++) {
            Groceries g = groceries.get(i);
            System.out.printf("%d. %s - RM%.2f (Stock: %d)\n", i + 1, g.getName(), g.getPrice(), g.getStockQuantity());
        }
        int choice = InputUtils.readInt(scanner, "Enter grocery number (or 0 to cancel): ") - 1;
        if (choice == -1) {
            return;
        }
        if (choice < 0 || choice >= groceries.size()) {
            System.out.println("Invalid grocery number. Please enter a number between 1 and " + groceries.size() + " (or 0 to cancel).");
            return;
        }
        Groceries selected = groceries.get(choice);
        int quantity = InputUtils.readInt(scanner, "Enter quantity: ");
        if (quantity <= 0) {
            System.out.println("Quantity must be > 0.");
            return;
        }
        if (quantity > selected.getStockQuantity()) {
            System.out.println("Not enough stock available.");
            return;
        }
        // Add to cart
        String userId = (customer != null) ? customer.getId() : "guest";
        Cart cart = cartRepository.loadCart(userId);
        cart.addItem(selected, quantity);
        cartRepository.saveCart(userId, cart);
        if (customer != null) {
            session.setCurrentCart(cart);
        }
        System.out.println("Added to cart successfully!");
    }
}
