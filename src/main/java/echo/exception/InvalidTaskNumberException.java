package echo.exception;

/**
 * Signals that a mark, unmark, or delete command referred to a task number
 * outside the current list.
 */
public class InvalidTaskNumberException extends EchoException {
    /**
     * Creates an error asking the user to pick a valid task number.
     */
    public InvalidTaskNumberException() {
        super("Please provide a valid task number."
                + " Check the list and choose a valid task number.");
    }
}
