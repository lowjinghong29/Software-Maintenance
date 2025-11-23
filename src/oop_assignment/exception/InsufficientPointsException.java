package oop_assignment.exception;

/**
 * Exception for insufficient points errors.
 */
public class InsufficientPointsException extends TrapstarException {
    public InsufficientPointsException(String message) {
        super(message);
    }

    public InsufficientPointsException(String message, Throwable cause) {
        super(message, cause);
    }
}
