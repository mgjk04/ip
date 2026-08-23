package echo.exception;
/**
 * A {@link EchoException} for when the command for creating a {@link echo.task.Todo} does not match the expected format.
 */
public class TodoFormatException extends EchoException {
    /** Creates the error shown when a todo command has no description. */
    public TodoFormatException() {
        super("Invalid todo command. Format: todo <description>");
    }
}
