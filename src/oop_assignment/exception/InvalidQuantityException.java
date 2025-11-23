package oop_assignment.exception;

/**
 * Exception for invalid quantities.
 */
public class InvalidQuantityException extends TrapstarException {
    public InvalidQuantityException(String message) {
        super(message);
    }

    public InvalidQuantityException(String message, Throwable cause) {
        super(message, cause);
    }
}
