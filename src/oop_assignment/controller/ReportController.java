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
            System.out.println("=== Reports Menu ===");
            System.out.println("1. View total revenue");
            System.out.println("2. View quantity sold by product");
            System.out.println("0. Back");
            int code = InputUtils.readInt(scanner, "Enter option: ");
            switch (code) {
                case 1:
                    double total = reportService.getTotalRevenue();
                    System.out.println("Total revenue: RM" + String.format("%.2f", total));
                    break;
                case 2:
                    Map<String, Integer> map = reportService.getTotalQuantityByProductId();
                    for (Map.Entry<String, Integer> entry : map.entrySet()) {
                        System.out.println("Product " + entry.getKey() + ": " + entry.getValue() + " units sold");
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
