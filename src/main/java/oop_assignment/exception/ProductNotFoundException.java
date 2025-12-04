package oop_assignment.exception;

/**
 * Exception for product not found errors.
 */
public class ProductNotFoundException extends TrapstarException {
    public ProductNotFoundException(String message) {
        super(message);
    }

}
