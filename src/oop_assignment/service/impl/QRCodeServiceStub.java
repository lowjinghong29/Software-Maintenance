package oop_assignment.service.impl;

import oop_assignment.service.QRCodeService;

/**
 * Stub implementation of QRCodeService for testing and development purposes.
 *
 * This class simulates QR code generation without actually creating or displaying QR codes.
 * In a real implementation, this would integrate with a QR code library (e.g., ZXing) to generate
 * and possibly display or save the QR code for payment processing.
 *
 * For now, it simply prints the content to the console, which can be used for debugging
 * or manual verification during checkout.
 */
public class QRCodeServiceStub implements QRCodeService {

    /**
     * Generates a payment QR code based on the provided content.
     *
     * In this stub implementation:
     * - The content typically includes payment details (e.g., total amount, order ID).
     * - Instead of generating a real QR code, it prints the content to the console.
     * - This allows the checkout process to proceed without external dependencies.
     *
     * @param content the string content to encode in the QR code (e.g., "Payment for RM25.00")
     */
    @Override
    public void generatePaymentCode(String content) {
        // Validate input to ensure robustness
        if (content == null || content.trim().isEmpty()) {
            System.err.println("Error: QR code content cannot be null or empty.");
            return;
        }

        // Stub behavior: Simulate QR code generation by printing to console
        // In a real app, this might save to a file, display in UI, or send to a payment gateway
        System.out.println("=== QR Code Generated ===");
        System.out.println("Content: " + content);
        System.out.println("Scan this QR code with your payment app to complete the transaction.");
        System.out.println("(This is a stub - no actual QR code image is created.)");
        System.out.println("==========================");
    }
}
