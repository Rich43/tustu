package az;

/**
 * Exception thrown when activation information cannot be parsed.
 */
public class ActivationParseException extends Exception {
    public ActivationParseException(String message) {
        super(message);
    }

    public ActivationParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
