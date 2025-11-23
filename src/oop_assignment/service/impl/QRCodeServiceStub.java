package oop_assignment.service.impl;

import oop_assignment.service.QRCodeService;

/**
 * Implementation of QRCodeService that generates a simple ASCII QR code for payment.
 */
public class QRCodeServiceStub implements QRCodeService {

    @Override
    public void generatePaymentCode(String content) {
        if (content == null || content.trim().isEmpty()) {
            System.err.println("Error: QR code content cannot be null or empty.");
            return;
        }

        System.out.println("=== QR Code Generated ===");
        System.out.println("Content: " + content);
        System.out.println("Scan the following QR code with your payment app to complete the transaction.");
        System.out.println();

        // Simple ASCII QR code representation (placeholder)
        printAsciiQRCode();

        System.out.println();
        System.out.println("==========================");
    }

    private void printAsciiQRCode() {
        // A simple 9x9 ASCII QR code pattern as placeholder
        String[] qr = {
            "####### ##",
            "#     #  #",
            "# ### # ##",
            "# ### # ##",
            "# ### #  #",
            "#     # ##",
            "#######  #",
            "         #",
            "##########"
        };
        for (String line : qr) {
            System.out.println(line);
        }
    }
}
