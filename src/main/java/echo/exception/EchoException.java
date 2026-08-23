package echo.exception;

/**
 * The base exception for all user-facing errors reported by the chatbot.
 * Prepends a distinctive prefix so users can tell bot errors apart from
 * other output.
 */
public class EchoException extends Exception {
    /**
     * Creates an error with the given message, prefixed for display.
     *
     * @param message explanation of what went wrong
     */
    public EchoException(String message) {
        super("OOPS!!! " + message);
    }
}
