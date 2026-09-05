package echo.exception;

/**
 * An {@link EchoException} created when the input task number does not correspond to an existing task.
 */
public class InvalidTaskNumberException extends EchoException {
    /** Creates the error shown when the given task number matches no task. */
    public InvalidTaskNumberException() {
        super("Please provide a valid task number. Check the list and choose a valid task number.");
    }
}
