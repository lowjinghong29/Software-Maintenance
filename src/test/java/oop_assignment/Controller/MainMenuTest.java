package oop_assignment.Controller;

import oop_assignment.controller.CheckoutController;
import oop_assignment.controller.MainMenuController;
import oop_assignment.controller.ReportController;
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
import java.util.List;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.io.IOException;
import java.util.List;


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
    private ReportController reportController;
    private ReportService reportService;

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
        reportService= new ReportServiceImpl(salesService);
        checkoutService = new CheckoutServiceImpl(pricingService, inventoryService, customerAccountService,
                customerService, salesService, receiptService, qrCodeService);

        // For tests, scanner input will be provided per test, so just create a dummy scanner
        checkoutController = new CheckoutController(new Scanner(""), inventoryService, checkoutService,
                receiptService, cartRepository);
        reportController = new ReportController(new Scanner(""),reportService,inventoryService);
    }

    @BeforeEach
    void resetFiles() throws IOException {
        // 1 Groceries
        Path groceriesPath = Path.of("src/groceries.txt");
        List<String> groceriesContent = List.of(
                "Apple,2.50,75",
                "Orange,3.00,76",
                "Milk,4.50,17",
                "Bread,2.00,28",
                "Eggs,0.50,190",
                "Chicken,10.00,10",
                "Rice,5.00,40",
                "Mango,6.00,26"
        );
        Files.write(groceriesPath, groceriesContent, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        //  Membership / Customers
        Path membershipPath = Path.of("src/membership.txt");
        List<String> membershipContent = List.of(
                "C001,John Moe,password123,newemail@example.com,019-1234566,123 Main St,50,100.00",
                "C002,Jane Smith,password456,jane@email.com,019-2345678,456 Elm St,30,200.00",
                "C003,Alice Johnson,password789,alice@email.com,019-3456789,789 Oak St,10,50.00",
                "C004,Bob Wilson,password000,bob@email.com,019-4567890,101 Pine St,20,75.00",
                "C005,Charlie Brown,password111,charlie@email.com,019-5678901,202 Oak St,40,160.00",
                "C006,Low Jing Hong,123321,jinghong@gmail.com,0124859965,52000 Kepong,0,1010.00",
                "C007,Shek Jun Yi,12345,junyi@gmail.com,0123950049,Seremban,0,101.00",
                "C008,Seow Theng Feng,1234,feng@gmail.com,0135944485,Gombak,64,1755.11",
                "C009,loo,123,1@gmail,123456,35 jalan 21/34,25,18.56"
        );
        Files.write(membershipPath, membershipContent, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        //  Carts
        Path cartsPath = Path.of("src/carts.txt");
        List<String> cartsContent = List.of
                (
                        "C002:Chicken,1;Beef,2;Pasta,3",
                        "C001:Bread,2;Eggs,6;Rice,1",
                        "C009:",
                        "guest:",
                        "C008:",
                        "C007:Milk,1",
                        "C006:Mango,1;Orange,1;Milk,1",
                        "C005:Eggs,12;Pasta,2;Chicken,1",
                        "C004:Bread,1;Milk,2;Rice,1",
                        "C003:Orange,4;Apple,1;Mango,2"
                );
        Files.write(cartsPath, cartsContent, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        //  Sales
        Path salesPath = Path.of("src/sales.txt");
        List<String> salesContent = List.of(
                "S001,Apple,Apple,2,5.00,2025-11-23T10:00:00,C001",
                            "S002,Banana,Banana,1,1.20,2025-11-23T10:05:00,guest",
                            "S003,Milk,Milk,1,4.50,2025-11-23T10:10:00,C002",
                            "S004,Orange,Orange,3,9.00,2025-11-23T10:15:00,C003",
                            "S005,Bread,Bread,2,4.00,2025-11-23T10:20:00,guest",
                            "S006,Eggs,Eggs,10,5.00,2025-11-23T10:25:00,C001",
                            "null,Apple,Apple,1,2.50,2025-11-23T22:54:05.736486100,null",
                            "null,Bread,Bread,1,2.00,2025-11-24T02:01:22.969160900,null",
                            "null,Apple,Apple,1,2.50,2025-11-24T02:07:35.367082,null",
                            "null,Apple,Apple,2,5.00,2025-11-24T02:26:32.738153900,null",
                            "null,Bread,Bread,3,6.00,2025-11-24T02:26:32.738153900,null",
                            "null,Milk,Milk,1,4.50,2025-11-24T02:26:32.738153900,null",
                            "null,Orange,Orange,4,12.00,2025-11-24T02:26:32.738153900,null",
                            "null,Mango,Mango,8,48.00,2025-11-24T02:29:04.778992800,null",
                            "null,Apple,Apple,4,10.00,2025-11-24T02:29:38.327844900,null",
                            "null,Orange,Orange,1,3.00,2025-11-24T02:29:38.327844900,null",
                            "null,Apple,Apple,1,2.50,2025-11-24T02:35:03.625868900,null",
                            "null,Bread,Bread,8,16.00,2025-11-24T02:39:23.522215,feng@gmail.com",
                            "null,Orange,Orange,1,3.00,2025-11-24T02:39:49.778485400,feng@gmail.com",
                            "null,Milk,Milk,2,9.00,2025-11-24T02:39:49.778485400,feng@gmail.com",
                            "null,Eggs,Eggs,10,5.00,2025-11-24T02:42:40.857721800,feng@gmail.com",
                            "null,Chicken,Chicken,10,100.00,2025-11-24T02:42:40.857721800,feng@gmail.com",
                            "null,Mango,Mango,1,6.00,2025-11-24T02:42:40.857721800,feng@gmail.com",
                            "null,Apple,Apple,2,5.00,2025-11-24T02:46:29.428878400,feng@gmail.com",
                            "null,Rice,Rice,10,50.00,2025-11-24T02:56:44.211365100,feng@gmail.com",
                            "null,Apple,Apple,10,25.00,2025-11-24T02:56:44.211365100,feng@gmail.com",
                            "null,Orange,Orange,2,6.00,2025-11-24T02:59:11.291490400,feng@gmail.com",
                            "null,Milk,Milk,10,45.00,2025-11-24T02:59:11.291490400,feng@gmail.com",
                            "null,Apple,Apple,1,2.50,2025-11-24T02:59:11.291490400,feng@gmail.com",
                            "null,Mango,Mango,4,24.00,2025-11-28T12:57:34.036174700,1@gmail",
                            "null,Orange,Orange,1,3.00,2025-11-28T13:51:54.951018900,1@gmail",
                            "null,Mango,Mango,1,6.00,2025-12-04T13:45:01.920142300,null"
        );
        Files.write(salesPath, salesContent, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        // Recreate repositories and services after reset
        groceriesRepository = new FileGroceriesRepository("src/groceries.txt");
        customerRepository = new FileCustomerRepository("src/membership.txt");
        cartRepository = new FileCartRepository("src/carts.txt", inventoryService);
        salesRepository = new FileSalesRepository("src/sales.txt");

        inventoryService = new InventoryServiceImpl(groceriesRepository);
        customerService = new CustomerServiceImpl(customerRepository);
        customerAccountService = new CustomerAccountServiceImpl(customerRepository);
        salesService = new SalesServiceImpl(salesRepository);
    }

    @Test
    void testMainMenuInvalidInputsAndCustomerSignUp() {
        // Input sequence:
        // 999  -> invalid menu option
        // abc  -> alphabet, should trigger InputMismatch
        // 2    -> Customer services
        // 3    -> Sign up
        // then fill form: name, email, phone, address, password
        String input = String.join("\n",
                "999",                // invalid number option
                "abc",                // alphabet
                "2",                  // go to Customer Services
                "3",                  // Sign up
                "Klee",   // Name
                "klee@example.com",  // Email
                "0123456789",         // Phone
                "123 Fantasy Road",   // Address
                "mypassword",         // Password
                "0"                   // exit after sign up (returns to main)
        ) + "\n";

        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);

        MainMenuController controller = new MainMenuController(
                scanner,
                checkoutController,
                null,
                reportController,
                session,
                authService,
                customerService,
                inventoryService,
                cartRepository,
                customerAccountService
        );

        controller.start();

        // ASSERT CUSTOMER WAS REGISTERED
        Customer registered = authService.customerLogin("klee@example.com", "mypassword");
        assertNotNull(registered, "Customer should be successfully registered");
        assertEquals("Klee", registered.getName());
        assertEquals("0123456789", registered.getPhoneNumber());
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
                reportController,
                session,
                authService,
                customerService,
                inventoryService,
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
                reportController,
                session,
                authService,
                customerService,
                inventoryService,
                cartRepository,
                customerAccountService
        );

        controller.handleRedemption();
        assertEquals(150, customer.getLoyaltyPoints());
        assertEquals(1, customer.getDiscountVouchers().get("RM2").intValue());
    }

    @Test
    void testGuestGrocerAndLogin() {
        String input = String.join("\n",
                "2",
                "1",
                "2",
                "3",
                "k",
                "5",
                "1",
                "123",
                "5",
                "1@gmail",
                "123"
        ) + "\n"; // make sure last line has newline for nextLine()

        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);
        checkoutController = new CheckoutController(scanner, inventoryService, checkoutService,
                receiptService, cartRepository);
        MainMenuController controller = new MainMenuController(
                scanner,
                checkoutController,
                null,
                reportController,
                session,
                authService,
                customerService,
                inventoryService,
                cartRepository,
                customerAccountService
        );

        controller.start();
        Customer loggedIn = session.getCurrentCustomer();
        assertNotNull(loggedIn, "A customer should now be logged in.");
        assertEquals("1@gmail", loggedIn.getEmail());

    }

    @Test
    void testGuestAddtoCart() {
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
                reportController,
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
    void testCheckoutCurrentCart() {
        String input = String.join("\n",
                "1", // Guest Menu: Purchase Groceries (guestMode.nextInt)
                "1", // Select Apple (readInt)
                "1", // Quantity (readInt)
                "0", // Finish selection (readInt)
                "n", // Do not proceed to checkout (scanner.nextLine)
                "4",//view cart
                "1",//checkout current cart
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
                reportController,
                session,
                authService,
                customerService,
                inventoryService,
                cartRepository,
                customerAccountService
        );

        controller.guestMode();

        Cart cart = cartRepository.loadCart("guest");
        assertTrue(cart.getItems().isEmpty(), "Cart should be empty after guest checkout");
    }

    @Test
    void testRemoveItemCart() {
        String input = String.join("\n",
                "1", // Guest Menu: Purchase Groceries (guestMode.nextInt)
                "1", // Select Apple (readInt)
                "1", // Quantity (readInt)
                "0", // Finish selection (readInt)
                "n", // Do not proceed to checkout (scanner.nextLine)
                "4",//view cart
                "2",//remove item
                "1",
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
                reportController,
                session,
                authService,
                customerService,
                inventoryService,
                cartRepository,
                customerAccountService
        );

        controller.guestMode();

        Cart cart = cartRepository.loadCart("guest");
        assertTrue(cart.getItems().isEmpty(), "Cart should be empty after guest checkout");

    }

    @Test
    void testClearAllCart() {
        String input = String.join("\n",
                "1", // Guest Menu: Purchase Groceries (guestMode.nextInt)
                "1", // Select Apple (readInt)
                "1", // Quantity (readInt)
                "0", // Finish selection (readInt)
                "n", // Do not proceed to checkout (scanner.nextLine)
                "4",//view cart
                "3",//remove all item
                "Y",
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
                reportController,
                session,
                authService,
                customerService,
                inventoryService,
                cartRepository,
                customerAccountService
        );

        controller.guestMode();

        Cart cart = cartRepository.loadCart("guest");
        assertTrue(cart.getItems().isEmpty(), "Cart should be empty after guest checkout");

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
                reportController,
                session,
                authService,
                customerService,
                inventoryService,
                cartRepository,
                customerAccountService
        );

        controller.handleViewCart();

        Cart cart = cartRepository.loadCart(customer.getId());
        assertEquals(0, cart.getSubtotal(), 0.001);
    }

    @Test
    void testStaffloginAndModifyCustomer() {
        String input = String.join("\n",
                "1",                  // go to staff login
                "m",// wrong username
                "admin",//right password
                "1",
                "admin",// right username
                "admin",//right password
                "a", //test alphabet input in menu
                "9", // test invalid menu
                "1",//modify customer
                "1",// modify first user
                "Urara",
                "2@gmail",
                "123-512",
                "tracen"
        ) + "\n";

        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);

        MainMenuController controller = new MainMenuController(
                scanner,
                checkoutController,
                null,
                reportController,
                session,
                authService,
                customerService,
                inventoryService,
                cartRepository,
                customerAccountService
        );

        controller.start();

        // ASSERT CUSTOMER WAS REGISTERED
        Customer updated = customerRepository.findAll().get(0);
        assertEquals("Urara", updated.getName());
        assertEquals("2@gmail", updated.getEmail());
        assertEquals("123-512", updated.getPhoneNumber());
        assertEquals("tracen", updated.getMailingAddress());
    }

    @Test
    void testSortAddGroceries() {
        String input = String.join("\n",
                "1",//staff login
                "admin", //username
                "admin", // password
                "5",
                "1",
                "2",
                "3",
                "0",
                "2",
                "1",
                "parfait",
                "12",
                "44"
        ) + "\n";

        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);

        MainMenuController controller = new MainMenuController(
                scanner,
                checkoutController,
                null,
                reportController,
                session,
                authService,
                customerService,
                inventoryService,
                cartRepository,
                customerAccountService
        );

        controller.start();

        // ASSERT item  WAS REGISTERED
        Groceries added = groceriesRepository.findAll().get(groceriesRepository.findAll().size() - 1);;
        assertEquals("parfait", added.getName());
        assertEquals(12.0, added.getPrice());
        assertEquals(44, added.getStockQuantity());
    }

    @Test
    void testRemoveGroceries() {
        int initialSize = groceriesRepository.findAll().size();
        String input = String.join("\n",
                "1",//staff login
                "admin", //username
                "admin", // password
                "2",
                "2",//remove
                "8"// parfait number
        ) + "\n";

        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);

        MainMenuController controller = new MainMenuController(
                scanner,
                checkoutController,
                null,
                reportController,
                session,
                authService,
                customerService,
                inventoryService,
                cartRepository,
                customerAccountService
        );

        controller.start();

        List<Groceries> finalList = groceriesRepository.findAll();

        assertEquals(initialSize - 1, finalList.size());

        boolean exists = finalList.stream()
                .anyMatch(g -> g.getName().equalsIgnoreCase("Mango"));

        assertFalse(exists, "Mango should have been removed");

    }

    @Test
    void testAddStock() {
        String input = String.join("\n",
                "1",//staff login
                "admin", //username
                "admin", // password
                "3",//addstock
                "8",//mango
                "-4",// negative quantity,
                "3",
                "8",
                "a",// alphabet input for quantity
                "10"
        ) + "\n";

        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);

        MainMenuController controller = new MainMenuController(
                scanner,
                checkoutController,
                null,
                reportController,
                session,
                authService,
                customerService,
                inventoryService,
                cartRepository,
                customerAccountService
        );

        controller.start();

        // assert quantity changed
        List<Groceries> finalList = groceriesRepository.findAll();
        // check if mango is added correctly
        assertEquals(36, finalList.get(finalList.size()-1).getStockQuantity());
    }

    @Test
    void testReport() {
        String input = String.join("\n",
                "1",//staff login
                "admin", //username
                "admin", // password
                "4",//report
                "1",//get total revenue
                "2"//pie chart
        ) + "\n";

        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);
        reportController = new ReportController(scanner, reportService, inventoryService);
        MainMenuController controller = new MainMenuController(
                scanner,
                checkoutController,
                null,
                reportController,
                session,
                authService,
                customerService,
                inventoryService,
                cartRepository,
                customerAccountService
        );

        controller.start();
    }
}