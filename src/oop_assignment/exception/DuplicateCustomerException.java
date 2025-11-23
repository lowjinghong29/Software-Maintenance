package oop_assignment.exception;

/**
 * Exception for duplicate customer errors.
 */
public class DuplicateCustomerException extends TrapstarException {
    public DuplicateCustomerException(String message) {
        super(message);
    }

    public DuplicateCustomerException(String message, Throwable cause) {
        super(message, cause);
    }
}
