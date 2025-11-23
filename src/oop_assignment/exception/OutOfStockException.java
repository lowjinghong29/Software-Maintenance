package oop_assignment.exception;

/**
 * Exception for out of stock errors.
 */
public class OutOfStockException extends TrapstarException {
    public OutOfStockException(String message) {
        super(message);
    }

    public OutOfStockException(String message, Throwable cause) {
        super(message, cause);
    }
}
