package oop_assignment.exception;

/**
 * Exception for invalid user inputs.
 */
public class InvalidInputException extends TrapstarException {
    public InvalidInputException(String message) {
        super(message);
    }

    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
