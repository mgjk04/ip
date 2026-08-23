package echo.exception;

/**
 * Signals that a mark or unmark command was entered without a task number
 * or with text that is not a whole number.
 */
public class TaskNumberFormatException extends EchoException {
    /**
     * Creates an error showing the correct format for the given command.
     *
     * @param keyword command keyword whose format was violated
     */
    public TaskNumberFormatException(String keyword) {
        super("Invalid " + keyword + " command. Format: " + keyword + " <taskNumber>");
    }
}
