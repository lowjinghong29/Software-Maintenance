package oop_assignment.exception;

/**
 * Exception for customer not found errors.
 */
public class CustomerNotFoundException extends TrapstarException {
    public CustomerNotFoundException(String message) {
        super(message);
    }

    public CustomerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
