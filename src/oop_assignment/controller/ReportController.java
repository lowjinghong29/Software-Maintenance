package oop_assignment.controller;

import oop_assignment.constant.Messages;
import oop_assignment.service.ReportService;
import oop_assignment.util.InputUtils;
import java.util.Map;
import java.util.Scanner;

/**
 * Controller for handling report interactions.
 */
public class ReportController {
    private final Scanner scanner;
    private final ReportService reportService;

    public ReportController(Scanner scanner, ReportService reportService) {
        if (scanner == null || reportService == null) {
            throw new IllegalArgumentException("Dependencies cannot be null");
        }
        this.scanner = scanner;
        this.reportService = reportService;
    }

    public void start() {
        while (true) {
            System.out.println(Messages.REPORTS_MENU_HEADER);
            System.out.println("1. View total revenue");
            System.out.println("2. View quantity sold by product");
            System.out.println("0. Back");
            int code = InputUtils.readInt(scanner, Messages.MAIN_MENU_PROMPT);
            switch (code) {
                case 1:
                    double totalRevenue = reportService.getTotalRevenue();
                    System.out.println(Messages.TOTAL_REVENUE + String.format("%.2f", totalRevenue));
                    break;
                case 2:
                    Map<String, Integer> quantityByProduct = reportService.getTotalQuantityByProductId();
                    System.out.println(Messages.QUANTITY_SOLD_HEADER);
                    if (quantityByProduct.isEmpty()) {
                        System.out.println(Messages.NO_SALES_DATA);
                    } else {
                        for (Map.Entry<String, Integer> entry : quantityByProduct.entrySet()) {
                            System.out.println("  " + entry.getKey() + ": " + entry.getValue() + " units");
                        }
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println(Messages.INVALID_OPTION);
            }
        }
    }
}
