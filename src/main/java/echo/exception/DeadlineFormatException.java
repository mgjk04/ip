package echo.exception;

/**
 * Signals that the deadline command was entered with missing arguments or
 * a date in the wrong format.
 */
public class DeadlineFormatException extends EchoException {
    /**
     * Creates an error that shows the correct deadline command format.
     */
    public DeadlineFormatException() {
        super("Invalid deadline command or date. Format: deadline <description>"
                + " /by <yyyy-MM-dd HHmm>, e.g., deadline return book /by 2019-12-02 1800");
    }
}
