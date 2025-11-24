package oop_assignment.service;

/**
 * Service for QR code generation.
 */
public interface QRCodeService {

    /**
     * Generates a payment QR code.
     * @param content the content for the QR code
     */
    void generatePaymentCode(String content);
}
