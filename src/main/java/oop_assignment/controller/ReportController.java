package oop_assignment.controller;

import oop_assignment.constant.Messages;
import oop_assignment.service.ReportService;
import oop_assignment.service.InventoryService;
import oop_assignment.util.InputUtils;
import java.util.Map;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Controller for handling report interactions.
 */
public class ReportController {
    private final Scanner scanner;
    private final ReportService reportService;
    private final InventoryService inventoryService;

    public ReportController(Scanner scanner, ReportService reportService, InventoryService inventoryService) {
        if (scanner == null || reportService == null || inventoryService == null) {
            throw new IllegalArgumentException("Dependencies cannot be null");
        }
        this.scanner = scanner;
        this.reportService = reportService;
        this.inventoryService = inventoryService;
    }

    public void start() {
        while (true) {
            System.out.println(Messages.REPORTS_MENU_HEADER);
            System.out.println("1. View total revenue");
            System.out.println("2. View quantity sold by product (with pie chart)");
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
                        // Get all product names from inventory
                        List<String> allProductNames = inventoryService.getAllGroceries().stream()
                                .map(g -> g.getName())
                                .collect(Collectors.toList());
                        // Generate pie chart with all categories
                        generateSalesPieChart(quantityByProduct, allProductNames);
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println(Messages.INVALID_OPTION);
            }
        }
    }

    /**
     * Generates and displays a pie chart for sales by category.
     * Ensures all categories are included, even with zero sales.
     */
    public void generateSalesPieChart(Map<String, Integer> salesByCategory, List<String> allCategories) {
        // Create a map with all categories, defaulting to 0, sorted by name
        Map<String, Integer> completeTotals = new java.util.TreeMap<>();
        for (String cat : allCategories) {
            completeTotals.put(cat, salesByCategory.getOrDefault(cat, 0));
        }

        // Display pie chart
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Sales Report Pie Chart");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(800, 400); // wider for legend
            frame.add(new PieChartPanel(completeTotals));
            frame.setVisible(true);
        });
    }

    /**
     * Panel for displaying pie chart of sales data.
     * Includes all categories with labels inside the pie slices.
     */
    static class PieChartPanel extends JPanel {
        private final Map<String, Integer> quantityByProduct;

        public PieChartPanel(Map<String, Integer> quantityByProduct) {
            this.quantityByProduct = quantityByProduct;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int totalQuantity = quantityByProduct.values().stream().mapToInt(Integer::intValue).sum();
            if (totalQuantity == 0) {
                g2d.drawString("No sales data available", 50, 50);
                return;
            }
            int startAngle = 0;
            int idx = 0;
            int centerX = 200; // center of pie
            int centerY = 200;
            int radius = 150;

            for (Map.Entry<String, Integer> entry : quantityByProduct.entrySet()) {
                String product = entry.getKey();
                int quantity = entry.getValue();
                int angle = (int) Math.round(360.0 * quantity / totalQuantity);
                if (angle == 0 && quantity > 0) angle = 1; // ensure small slices are visible

                // Draw slice
                g2d.setColor(getDeterministicColor(idx));
                g2d.fill(new Arc2D.Double(centerX - radius, centerY - radius, 2 * radius, 2 * radius, startAngle, angle, Arc2D.PIE));

                startAngle += angle;
                idx++;
            }

            // Draw legend
            int legendX = 400;
            int legendY = 50;
            idx = 0;
            for (Map.Entry<String, Integer> entry : quantityByProduct.entrySet()) {
                g2d.setColor(getDeterministicColor(idx));
                g2d.fillRect(legendX, legendY, 20, 20);
                g2d.setColor(Color.BLACK);
                double percentage = (entry.getValue() * 100.0) / totalQuantity;
                g2d.drawString(entry.getKey() + ": " + entry.getValue() + " (" + String.format("%.1f", percentage) + "%)", legendX + 25, legendY + 15);
                legendY += 25;
                idx++;
            }
        }

        private Color getDeterministicColor(int index) {
            Color[] palette = {
                Color.BLUE, Color.RED, Color.GREEN, Color.ORANGE, Color.CYAN,
                Color.MAGENTA, Color.YELLOW, Color.PINK, Color.GRAY, Color.DARK_GRAY
            };
            return palette[index % palette.length];
        }
    }
}
