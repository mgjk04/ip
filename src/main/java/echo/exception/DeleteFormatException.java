package echo.exception;

/**
 * Signals that the delete command was entered without a task number or
 * with an unusable one.
 */
public class DeleteFormatException extends EchoException {
    /**
     * Creates an error that shows the correct delete command format.
     */
    public DeleteFormatException() {
        super("Invalid delete command. Format: delete <taskNumber>");
    }
}
