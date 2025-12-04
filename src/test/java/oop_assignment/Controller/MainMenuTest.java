package oop_assignment.Controller;

import oop_assignment.controller.CheckoutController;
import oop_assignment.controller.MainMenuController;
import oop_assignment.model.*;
import oop_assignment.repository.*;
import oop_assignment.repository.file.*;
import oop_assignment.service.*;
import oop_assignment.service.impl.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class MainMenuTest {

    private Session session;
    private AuthService authService;
    private CustomerService customerService;
    private CheckoutController checkoutController;
    private CustomerAccountService customerAccountService;
    private CartRepository cartRepository;
    private CustomerRepository customerRepository;
    private StaffRepository staffRepository;
    private GroceriesRepository groceriesRepository;
    private InventoryService inventoryService;
    private CheckoutService checkoutService;
    private ReceiptService receiptService;
    private PricingService pricingService;
    private SalesService salesService;
    private QRCodeService qrCodeService;
    private SalesRepository salesRepository;

    @BeforeEach
    void setUp() {
        session = new Session();
        customerRepository = new FileCustomerRepository("src/membership.txt");
        staffRepository = new FileStaffRepository("src/staff.txt");
        customerService = new CustomerServiceImpl(customerRepository);
        authService = new AuthServiceImpl(customerRepository, staffRepository, customerService);
        groceriesRepository = new FileGroceriesRepository("src/groceries.txt");
        inventoryService = new InventoryServiceImpl(groceriesRepository);
        cartRepository = new FileCartRepository("src/carts.txt", inventoryService);
        customerAccountService = new CustomerAccountServiceImpl(customerRepository);
        receiptService = new ReceiptServiceImpl();
        pricingService = new PricingServiceImpl();
        salesRepository = new FileSalesRepository("src/sales.txt");
        salesService = new SalesServiceImpl(salesRepository);
        qrCodeService = new QRCodeServiceStub();
        checkoutService = new CheckoutServiceImpl(pricingService, inventoryService, customerAccountService,
                customerService, salesService, receiptService, qrCodeService);

        // For tests, scanner input will be provided per test, so just create a dummy scanner
        checkoutController = new CheckoutController(new Scanner(""), inventoryService, checkoutService,
                receiptService, cartRepository);
    }

    @Test
    void testCustomerLoginAndTopUp() {
        Customer customer = new Customer("C999", "Alice", "pass", "alice@example.com",
                "123456", "Address", 100, 100.0);
        authService.registerCustomer(customer);

        // Input: login -> top-up -> back to main menu
        String input = String.join("\n",
                "2",                  // Login as member
                "alice@example.com",  // Email
                "pass",               // Password
                "4",                  // Top-up wallet
                "50",                 // Amount
                "0"
        ) + "\n";

        InputStream in = new ByteArrayInputStream(input.getBytes());
        MainMenuController controller = new MainMenuController(
                new Scanner(in),
                checkoutController,
                null,
                null,
                session,
                authService,
                customerService,
                null,
                cartRepository,
                customerAccountService
        );

        controller.handleCustomer();
        Customer current = session.getCurrentCustomer();
        assertEquals(150.0, current.getBalance());
    }

    @Test
    void testRedemption() {
        Customer customer = new Customer("C998", "Bob", "pass", "bob@example.com",
                "123456", "Address", 200, 100.0);
        customer.setDiscountVouchers(new HashMap<>());
        session.setCurrentCustomer(customer);

        String input = String.join("\n",
                "1", // Redeem 50 points
                "Y", // Confirm
                "4", // View vouchers
                "0"  // Back
        ) + "\n";

        InputStream in = new ByteArrayInputStream(input.getBytes());
        MainMenuController controller = new MainMenuController(
                new Scanner(in),
                checkoutController,
                null,
                null,
                session,
                authService,
                customerService,
                null,
                cartRepository,
                customerAccountService
        );

        controller.handleRedemption();
        assertEquals(150, customer.getLoyaltyPoints());
        assertEquals(1, customer.getDiscountVouchers().get("RM2").intValue());
    }

    @Test
    void testGuestFlow() {
        String input = String.join("\n",
                "1", // Guest Menu: Purchase Groceries (guestMode.nextInt)
                "1", // Select Apple (readInt)
                "3", // Quantity (readInt)
                "0", // Finish selection (readInt)
                "n", // Do not proceed to checkout (scanner.nextLine)
                "0"  // Exit Guest Menu (guestMode.nextInt)
        ) + "\n"; // make sure last line has newline for nextLine()

        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);
        checkoutController = new CheckoutController(scanner, inventoryService, checkoutService,
                receiptService, cartRepository);
        MainMenuController controller = new MainMenuController(
                scanner,
                checkoutController,
                null,
                null,
                session,
                authService,
                customerService,
                inventoryService,
                cartRepository,
                customerAccountService
        );

        controller.guestMode();

        Cart cart = cartRepository.loadCart("guest");
        assertNotNull(cart);
        assertEquals(1, cart.getItems().size());
        assertEquals("Apple", cart.getItems().get(0).getItem().getName());
        assertEquals(3, cart.getItems().get(0).getQuantity());
    }
    @Test
    void testViewCartEmpty() {
        Customer customer = new Customer("C001", "Alice", "pass", "alice@example.com",
                "123456", "Address", 100, 100.0);
        session.setCurrentCustomer(customer);

        cartRepository.saveCart(customer.getId(), new Cart());

        String input = "4\n0\n"; // Continue shopping -> back
        InputStream in = new ByteArrayInputStream(input.getBytes());
        MainMenuController controller = new MainMenuController(
                new Scanner(in),
                checkoutController,
                null,
                null,
                session,
                authService,
                customerService,
                null,
                cartRepository,
                customerAccountService
        );

        controller.handleViewCart();

        Cart cart = cartRepository.loadCart(customer.getId());
        assertEquals(0, cart.getSubtotal(), 0.001);
    }
}