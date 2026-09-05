package echo.exception;

/**
 * A {@link EchoException} for when the command for creating a {@link echo.task.Deadline} does not match the
 * expected format.
 */
public class DeadlineFormatException extends EchoException {
    /**
     * Creates a {@link DeadlineFormatException}.
     */
    public DeadlineFormatException() {
        super("Invalid deadline command or date. Format: deadline <description> /by <yyyy-MM-dd HHmm>, e.g., deadline "
                + "return book /by 2019-12-02 1800");
    }
}
