package oop_assignment.exception;

/**
 * Exception for authentication failures.
 */
public class AuthenticationFailedException extends TrapstarException {
    public AuthenticationFailedException(String message) {
        super(message);
    }

}
