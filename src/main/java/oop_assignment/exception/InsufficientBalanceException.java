package oop_assignment.exception;

/**
 * Exception for insufficient balance errors.
 */
public class InsufficientBalanceException extends TrapstarException {
    public InsufficientBalanceException(String message) {
        super(message);
    }

}
