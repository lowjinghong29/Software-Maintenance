package oop_assignment;

import oop_assignment.controller.CheckoutController;
import oop_assignment.controller.MainMenuController;
import oop_assignment.controller.MembershipController;
import oop_assignment.controller.ReportController;
import oop_assignment.model.Session;
import oop_assignment.repository.CustomerRepository;
import oop_assignment.repository.GroceriesRepository;
import oop_assignment.repository.SalesRepository;
import oop_assignment.repository.file.FileCustomerRepository;
import oop_assignment.repository.file.FileGroceriesRepository;
import oop_assignment.repository.file.FileSalesRepository;
import oop_assignment.service.AuthService;
import oop_assignment.service.CheckoutService;
import oop_assignment.service.CustomerAccountService;
import oop_assignment.service.CustomerService;
import oop_assignment.service.InventoryService;
import oop_assignment.service.PricingService;
import oop_assignment.service.ReceiptService;
import oop_assignment.service.ReportService;
import oop_assignment.service.SalesService;
import oop_assignment.service.QRCodeService;
import oop_assignment.service.impl.AuthServiceImpl;
import oop_assignment.service.impl.CheckoutServiceImpl;
import oop_assignment.service.impl.CustomerAccountServiceImpl;
import oop_assignment.service.impl.CustomerServiceImpl;
import oop_assignment.service.impl.InventoryServiceImpl;
import oop_assignment.service.impl.PricingServiceImpl;
import oop_assignment.service.impl.ReceiptServiceImpl;
import oop_assignment.service.impl.ReportServiceImpl;
import oop_assignment.service.impl.SalesServiceImpl;
import oop_assignment.service.impl.QRCodeServiceStub;
import java.util.Scanner;

/**
 * New entry point for the refactored Trapstar Groceries application.
 */
public class MainApplication {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Session session = new Session();

        GroceriesRepository groceriesRepository = new FileGroceriesRepository("groceries.txt");
        CustomerRepository customerRepository = new FileCustomerRepository("membership.txt");
        SalesRepository salesRepository = new FileSalesRepository("sales.txt");

        InventoryService inventoryService = new InventoryServiceImpl(groceriesRepository);
        PricingService pricingService = new PricingServiceImpl();
        CustomerService customerService = new CustomerServiceImpl(customerRepository);
        CustomerAccountService customerAccountService = new CustomerAccountServiceImpl(customerRepository);
        AuthService authService = new AuthServiceImpl(customerRepository);
        QRCodeService qrCodeService = new QRCodeServiceStub();
        CheckoutService checkoutService = new CheckoutServiceImpl(pricingService, inventoryService, customerAccountService, salesService, receiptService, qrCodeService);
        ReceiptService receiptService = new ReceiptServiceImpl();
        SalesService salesService = new SalesServiceImpl(salesRepository);
        ReportService reportService = new ReportServiceImpl(salesService);

        CheckoutController checkoutController = new CheckoutController(scanner, inventoryService, checkoutService, receiptService);
        MembershipController membershipController = new MembershipController(scanner, session, authService, customerService, customerAccountService);
        ReportController reportController = new ReportController(scanner, reportService);
        MainMenuController mainMenuController = new MainMenuController(scanner, checkoutController, membershipController, reportController, session);

        mainMenuController.start();
        scanner.close();
    }
}
