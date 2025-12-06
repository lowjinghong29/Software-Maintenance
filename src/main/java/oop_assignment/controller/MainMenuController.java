package oop_assignment.controller;

import oop_assignment.constant.Messages;
import oop_assignment.menu.MainMenuOption;
import oop_assignment.menu.*;
import oop_assignment.model.Customer;
import oop_assignment.model.Groceries;
import oop_assignment.model.Session;
import oop_assignment.model.Staff;
import oop_assignment.service.AuthService;
import oop_assignment.service.CustomerService;
import oop_assignment.service.InventoryService;
import oop_assignment.service.CustomerAccountService;
import oop_assignment.util.InputUtils;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
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
    private final ReportController reportController;
    private final Session session;
    private final AuthService authService;
    private final CustomerService customerService;
    private final InventoryService inventoryService;
    private final CartRepository cartRepository;
    private final CustomerAccountService customerAccountService;

    private static final String WELCOME_ART = "___________                             __                   ________                                .__               \n" +
"\\__    ___/___________  ______  _______/  |______ _______   /  _____/______  ____   ____  ___________|__| ____   ______\n" +
"  |    |  \\_  __ \\__  \\ \\____ \\/  ___/\\   __\\__  \\\\_  __ \\ /   \\  __\\_  __ \\/  _ \\_/ ___\\/ __ \\_  __ \\  |/ __ \\ /  ___/\n" +
"  |    |   |  | \\// __ \\|  |_> >___ \\  |  |  / __ \\|  | \\/ \\    \\_\\  \\  | \\(  <_> )  \\__\\  ___/|  | \\/  \\  ___/ \\___ \\ \n" +
"  |____|   |__|  (____  /   __/____  > |__| (____  /__|     \\______  /__|   \\____/ \\___  >___  >__|  |__|\\___  >____  >\n" +
"                      \\/|__|       \\/            \\/                \\/                  \\/    \\/              \\/     \\/\n";

    public MainMenuController(Scanner scanner, CheckoutController checkoutController,
                              ReportController reportController, Session session, AuthService authService, CustomerService customerService, InventoryService inventoryService, CartRepository cartRepository, CustomerAccountService customerAccountService) {
        this.scanner = scanner;
        this.checkoutController = checkoutController;
        this.reportController = reportController;
        this.session = session;
        this.authService = authService;
        this.customerService = customerService;
        this.inventoryService = inventoryService;
        this.cartRepository = cartRepository;
        this.customerAccountService = customerAccountService;
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

    public void handleRedemption() {
        if (!session.hasCustomer()) {
            System.out.println("\nPlease log in as a member to redeem points.");
            return;
        }
        Customer customer = session.getCurrentCustomer();
        while (true) {
            System.out.println("\n=== Redemption Menu ===");
            for (RedemptionMenuOption option : RedemptionMenuOption.values()) {
                System.out.println(option);
            }
            System.out.print(Messages.MAIN_MENU_PROMPT);
            try {
                int choice = InputUtils.readInt(scanner, "");
                RedemptionMenuOption option = RedemptionMenuOption.fromCode(choice);
                switch (option) {
                    case REDEEM_50:
                        redeemVoucher(customer, 50, "RM2");
                        break;
                    case REDEEM_100:
                        redeemVoucher(customer, 100, "RM5");
                        break;
                    case REDEEM_200:
                        redeemVoucher(customer, 200, "RM10");
                        break;
                    case VIEW_VOUCHERS:
                        viewVouchers(customer);
                        break;
                    case BACK :
                        return;
                }

            } catch (IllegalArgumentException e) {
                System.out.println(Messages.INVALID_OPTION);
                scanner.nextLine();
            }catch (InputMismatchException e) {
                System.out.println(Messages.INVALID_NUMBER);
                scanner.nextLine();
            }
        }
    }

    private void redeemVoucher(Customer customer, int pointsRequired, String voucherType) {
        if (customer.getLoyaltyPoints() < pointsRequired) {
            System.out.println("You need at least " + pointsRequired + " points to redeem this voucher. You currently have " + customer.getLoyaltyPoints() + " points.");
            System.out.println("Earn more points by making purchases!");
            return;
        }
        System.out.println("Redeem " + pointsRequired + " points for " + voucherType + " discount voucher? (Y/N)");
        String confirm = scanner.nextLine().trim().toUpperCase();
        if (confirm.equals("Y")) {
            customer.setLoyaltyPoints(customer.getLoyaltyPoints() - pointsRequired);
            Map<String, Integer> vouchers = customer.getDiscountVouchers();
            vouchers.put(voucherType, vouchers.getOrDefault(voucherType, 0) + 1);
            try {
                customerService.updateCustomer(customer);
                System.out.println("Redemption successful! You have redeemed a " + voucherType + " discount voucher.");
                System.out.println("Remaining points: " + customer.getLoyaltyPoints());
            } catch (Exception e) {
                System.out.println("Failed to redeem: " + e.getMessage());
            }
        } else {
            System.out.println("Redemption cancelled.");
        }
    }

    private void viewVouchers(Customer customer) {
        Map<String, Integer> vouchers = customer.getDiscountVouchers();
        System.out.println("\n=== Your Discount Vouchers ===");
        if (vouchers.isEmpty()) {
            System.out.println("You have no discount vouchers.");
        } else {
            for (Map.Entry<String, Integer> entry : vouchers.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue() + " voucher(s)");
            }
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
            for (StaffMenu option : StaffMenu.values()) {
                System.out.println(option); // calls option.toString()
            }
            System.out.print(Messages.MAIN_MENU_PROMPT);
            try {
                int code = scanner.nextInt();
                scanner.nextLine();
                StaffMenu choice = StaffMenu.fromCode(code);
                switch (choice) {
                    case MODIFY_CUSTOMER:
                        handleModifyCustomer();
                        break;
                    case ADD_REMOVE_GROCERIES:
                        handleAddRemoveGroceries();
                        break;
                    case ADD_STOCK:
                        handleAddStock();
                        break;
                    case GENERATE_REPORT:
                        reportController.start();
                        break;
                    case SORT_GROCERIES:
                        handleSortGroceries();
                        break;
                    case LOGOUT:
                        return;
                    default:
                        System.out.println(Messages.INVALID_OPTION);
                }
            }catch (IllegalArgumentException e) {
                System.out.println(Messages.INVALID_OPTION);
            }
            catch (InputMismatchException e) {
                System.out.println(Messages.INVALID_NUMBER);
                scanner.nextLine();
            }
        }
    }

    public void handleCustomer() {
        while (true) {
            if (session.hasCustomer()) {
                // Logged in menu
                System.out.println("\n=== Member Services ===");
                System.out.println("Welcome back, " + session.getCurrentCustomer().getName() + " (Points: " + session.getCurrentCustomer().getLoyaltyPoints() + ")!");
                for (CustomerMenuOption option : new CustomerMenuOption[]{
                        CustomerMenuOption.PURCHASE_GROCERIES,
                        CustomerMenuOption.VIEW_CART,
                        CustomerMenuOption.REDEMPTION,
                        CustomerMenuOption.TOP_UP,
                        CustomerMenuOption.LOGOUT,
                        CustomerMenuOption.BACK}) {
                    System.out.println(option);
                }
                System.out.print(Messages.MAIN_MENU_PROMPT);
                try {
                    int choice = scanner.nextInt();
                    scanner.nextLine(); // consume newline
                    CustomerMenuOption option = CustomerMenuOption.fromCode(choice);
                    switch (option) {
                        case PURCHASE_GROCERIES:
                            checkoutController.startCheckout(session.getCurrentCustomer());
                            break;
                        case VIEW_CART:
                            handleViewCart();
                            break;
                        case REDEMPTION:
                            handleRedemption();
                            break;
                        case TOP_UP:
                            // Direct top up
                            Customer customer = session.getCurrentCustomer();
                            System.out.println("Current balance: RM" + customer.getBalance());
                            System.out.print("Enter the amount to top up (RM): ");
                            try {
                                double amount = Double.parseDouble(scanner.nextLine().trim());
                                if (amount <= 0) {
                                    System.out.println("Amount must be greater than 0.");
                                    return;
                                }
                                customerAccountService.addBalance(customer, amount);
                                System.out.println("Top-up successful! New balance: RM" + customer.getBalance());
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid number. Please enter a valid amount.");
                            }
                            break;
                        case LOGOUT:
                            session.clearCustomer();
                            System.out.println(Messages.LOGOUT_MESSAGE);
                            break;
                        case BACK:
                            return;
                        default:
                            System.out.println(Messages.INVALID_OPTION);
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println(Messages.INVALID_OPTION);
                }catch (InputMismatchException e) {
                    System.out.println(Messages.INVALID_NUMBER);
                    scanner.nextLine();
                }
            } else {
                // Not logged in
                System.out.println("\n=== Customer Services ===");
                System.out.println("Are you a member?");
                for (CustomerMenuOption option : new CustomerMenuOption[]{
                        CustomerMenuOption.GUEST,
                        CustomerMenuOption.LOGIN,
                        CustomerMenuOption.SIGN_UP,
                        CustomerMenuOption.BACK }) {
                    System.out.println(option);
                }
                System.out.print(Messages.MAIN_MENU_PROMPT);
                try {
                    int choice = scanner.nextInt();
                    scanner.nextLine(); // consume newline
                    CustomerMenuOption option = CustomerMenuOption.fromCode(choice);
                    switch (option) {
                        case GUEST:
                            // Guest mode
                            guestMode();
                            break;
                        case LOGIN:
                            // Direct login
                            System.out.println("\n=== Member Login ===");
                            System.out.print("Enter member ID or email: ");
                            String idOrEmail = scanner.nextLine().trim();
                            System.out.print("Enter password: ");
                            String password = scanner.nextLine().trim();
                            try {
                                Customer customer = authService.customerLogin(idOrEmail, password);
                                session.setCurrentCustomer(customer);
                                System.out.println("Login successful! Welcome, " + customer.getName() + "!");
                            } catch (Exception e) {
                                System.out.println("Invalid credentials. Please try again.");
                            }
                            break;
                        case SIGN_UP:
                            handleSignUp();
                            break;
                        case BACK:
                            return;
                        default:
                            System.out.println(Messages.INVALID_OPTION);
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println(Messages.INVALID_OPTION);
                }catch (InputMismatchException e) {
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
            for (AddRemoveGroceriesOption option : AddRemoveGroceriesOption.values()) {
                System.out.println(option);
            }
            try {
            int choice = InputUtils.readInt(scanner, Messages.MAIN_MENU_PROMPT);
            AddRemoveGroceriesOption option = AddRemoveGroceriesOption.fromCode(choice);
            switch (option) {
                case ADD_GROCERY:
                    handleAddGrocery();
                    break;
                case REMOVE_GROCERY:
                    handleRemoveGrocery();
                    break;
                case BACK:
                    return;
            }
            } catch (IllegalArgumentException e) {
                System.out.println(Messages.INVALID_OPTION);
            } catch (InputMismatchException e)
            {
                System.out.println(Messages.INVALID_NUMBER);
                scanner.nextLine();
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
            return; // cancel
        }
        if (choice < 0 || choice >= groceries.size()) {
            System.out.println("Invalid grocery number. Please enter a number between 1 and " + groceries.size() + " (or 0 to cancel).");
            return;
        }
        Groceries selected = groceries.get(choice);
        try {
            inventoryService.removeGrocery(selected.getName());
            System.out.println("Grocery removed successfully!");
        } catch (Exception e) {
            System.out.println("Failed to remove grocery: " + e.getMessage());
        }
    }

    private void handleAddStock() {
        List<Groceries> groceries = inventoryService.getAllGroceries();
        if (groceries.isEmpty()) {
            System.out.println("No groceries available. Please add groceries first.");
            return;
        }
        System.out.println("Select grocery to add stock:");
        for (int i = 0; i < groceries.size(); i++) {
            Groceries g = groceries.get(i);
            System.out.printf("%d. %s (Current stock: %d)\n", i + 1, g.getName(), g.getStockQuantity());
        }
        int choice = InputUtils.readInt(scanner, "Enter grocery number (or 0 to cancel): ") - 1;
        if (choice == -1) {
            return; // cancel
        }
        if (choice < 0 || choice >= groceries.size()) {
            System.out.println("Invalid grocery number. Please enter a number between 1 and " + groceries.size() + " (or 0 to cancel).");
            return;
        }
        Groceries selected = groceries.get(choice);
        int additionalStock = InputUtils.readInt(scanner, "Enter additional stock for " + selected.getName() + ": ");
        if (additionalStock <= 0) {
            System.out.println("Additional stock must be > 0.");
            return;
        }
        selected.setStockQuantity(selected.getStockQuantity() + additionalStock);
        try {
            inventoryService.updateGrocery(selected);
            System.out.println("Stock updated successfully!");
        } catch (Exception e) {
            System.out.println("Failed to update stock: " + e.getMessage());
        }
    }

    private void handleSortGroceries() {
        while (true) {
            System.out.println("\n=== Sort Groceries ===");
            System.out.println("1. Sort by Name");
            System.out.println("2. Sort by Price");
            System.out.println("3. Sort by Stock");
            System.out.println("0. Back");
            int choice = InputUtils.readInt(scanner, Messages.MAIN_MENU_PROMPT);
            List<Groceries> groceries = inventoryService.getAllGroceries();
            if (groceries.isEmpty()) {
                System.out.println("No groceries available to sort.");
                return;
            }
            switch (choice) {
                case 1:
                    groceries.sort((g1, g2) -> g1.getName().compareToIgnoreCase(g2.getName()));
                    System.out.println("Groceries sorted by name.");
                    break;
                case 2:
                    groceries.sort((g1, g2) -> Double.compare(g1.getPrice(), g2.getPrice()));
                    System.out.println("Groceries sorted by price.");
                    break;
                case 3:
                    groceries.sort((g1, g2) -> Integer.compare(g1.getStockQuantity(), g2.getStockQuantity()));
                    System.out.println("Groceries sorted by stock.");
                    break;
                case 0:
                    return;
                default:
                    System.out.println(Messages.INVALID_OPTION);
            }
            // Display sorted groceries
            System.out.println("\n=== Sorted Groceries ===");
            for (Groceries g : groceries) {
                System.out.printf("Name: %s, Price: %.2f, Stock: %d\n", g.getName(), g.getPrice(), g.getStockQuantity());
            }
        }
    }

    public void guestMode() {
        while (true) {
            System.out.println("\n=== Guest Menu ===");
            for (GuestMenuOption option : GuestMenuOption.values()) {
                System.out.println(option);
            }
            System.out.print(Messages.MAIN_MENU_PROMPT);
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline
                GuestMenuOption option = GuestMenuOption.fromCode(choice);
                switch (option) {
                    case PURCHASE_GROCERIES:
                        checkoutController.startCheckout(null);
                        break;
                    case BROWSE_GROCERIES:
                        handleBrowseGroceries();
                        break;
                    case SEARCH_GROCERIES:
                        handleSearchGroceries();
                        break;
                    case VIEW_CART:
                        handleViewCart();
                        break;
                    case LOGIN_MEMBER:
                        handleMemberLogin();
                        break;
                    case BACK:
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

    private void handleBrowseGroceries() {
        List<Groceries> groceries = inventoryService.getAllGroceries();
        if (groceries.isEmpty()) {
            System.out.println("No groceries available.");
            return;
        }
        System.out.println("\n=== Browse Groceries ===");
        for (Groceries g : groceries) {
            System.out.printf("Name: %s, Price: %.2f, Stock: %d\n", g.getName(), g.getPrice(), g.getStockQuantity());
        }
    }

    private void handleSearchGroceries() {
        System.out.print("Enter keyword to search: ");
        String keyword = scanner.nextLine().trim();
        List<Groceries> results = inventoryService.searchGroceries(keyword);
        if (results.isEmpty()) {
            System.out.println("No groceries found matching your search.");
            return;
        }
        System.out.println("\n=== Search Results ===");
        for (Groceries g : results) {
            System.out.printf("Name: %s, Price: %.2f, Stock: %d\n", g.getName(), g.getPrice(), g.getStockQuantity());
        }
    }

    private void handleMemberLogin() {
        System.out.println("\n=== Member Login ===");
        System.out.print("Enter member ID or email: ");
        String idOrEmail = scanner.nextLine().trim();
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();
        try {
            Customer customer = authService.customerLogin(idOrEmail, password);
            session.setCurrentCustomer(customer);
            System.out.println("Login successful! Welcome, " + customer.getName() + "!");
        } catch (Exception e) {
            System.out.println("Invalid credentials. Please try again.");
        }
    }

    private void handleSignUp() {
        System.out.println("\n=== Sign Up ===");
        System.out.print("Enter your name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter your email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Enter your phone number: ");
        String phone = scanner.nextLine().trim();
        System.out.print("Enter your mailing address: ");
        String address = scanner.nextLine().trim();
        System.out.print("Set a password: ");
        String password = scanner.nextLine().trim();
        Customer newCustomer = new Customer("temp", name, password, email, phone, address, 0, 0.0);
        try {
            authService.registerCustomer(newCustomer);
            System.out.println("Registration successful! You can now log in.");
        } catch (Exception e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    public void handleViewCart() {
        String userId = session.hasCustomer() ? session.getCurrentCustomer().getId() : "guest";
        Cart cart = cartRepository.loadCart(userId);
        System.out.println("\n=== Your Cart ===");
        List<CartItem> cartItems = cart.getItems();
        if (cartItems.isEmpty()) {
            System.out.println("Your cart is empty.");
        } else {
            for (int i = 0; i < cartItems.size(); i++) {
                CartItem item = cartItems.get(i);
                System.out.printf("%d. %s (RM%.2f) [Qty: %d] = RM%.2f%n", i + 1, item.getItem().getName(), item.getItem().getPrice(), item.getQuantity(), item.getLineTotal());
            }
            System.out.printf("Total Quantity: %d%n", cart.getTotalQuantity());
            System.out.printf("Subtotal: RM%.2f%n", cart.getSubtotal());
        }
        System.out.println("===================");
        for (CartMenu option : CartMenu.values()) {
            System.out.println(option); // calls option.toString()
        }
        System.out.print(Messages.MAIN_MENU_PROMPT);
        try {
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            CartMenu option = CartMenu.fromCode(choice);
            switch (option) {
                case CHECKOUT:
                    Customer customer = session.hasCustomer() ? session.getCurrentCustomer() : null;
                    checkoutController.checkoutExistingCart(cart, customer);
                    break;
                case REMOVE_ITEM:
                    if (cartItems.isEmpty()) {
                        System.out.println("Cart is empty. Nothing to remove.");
                    } else {
                        System.out.print("Enter the item number to remove: ");
                        int itemNum = scanner.nextInt();
                        scanner.nextLine();
                        if (itemNum < 1 || itemNum > cartItems.size()) {
                            System.out.println("Invalid item number.");
                        } else {
                            CartItem toRemove = cartItems.get(itemNum - 1);
                            cart.removeItem(toRemove.getItem());
                            cartRepository.saveCart(userId, cart);
                            System.out.println("Item removed from cart.");
                        }
                    }
                    break;
                case CLEAR_CART:
                    System.out.print("Are you sure you want to clear the entire cart? (Y/N): ");
                    String confirm = scanner.nextLine().trim().toUpperCase();
                    if (confirm.equals("Y")) {
                        cartRepository.saveCart(userId, new Cart());
                        System.out.println("Cart cleared.");
                    } else {
                        System.out.println("Cart not cleared.");
                    }
                    break;
                case CONTINUE_SHOPPING:
                    // Do nothing, just return to menu
                    break;
            }
        }catch (IllegalArgumentException e) {
                System.out.println(Messages.INVALID_OPTION);
        } catch (InputMismatchException e) {
            System.out.println(Messages.INVALID_NUMBER);
            scanner.nextLine();
        }
    }

}
