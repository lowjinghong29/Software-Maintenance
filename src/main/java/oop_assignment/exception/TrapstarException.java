package oop_assignment.exception;

/**
 * Base exception for domain-specific errors in Trapstar Groceries.
 */
public class TrapstarException extends RuntimeException {
    public TrapstarException(String message) {
        super(message);
    }

    public TrapstarException(String message, Throwable cause) {
        super(message, cause);
    }
}
