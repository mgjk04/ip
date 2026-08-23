package echo.exception;

/**
 * Signals that the todo command was entered without a description.
 */
public class TodoFormatException extends EchoException {
    /**
     * Creates an error that shows the correct todo command format.
     */
    public TodoFormatException() {
        super("Invalid todo command. Format: todo <description>");
    }
}
