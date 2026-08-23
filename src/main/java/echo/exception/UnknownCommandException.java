package echo.exception;

/**
 * An {@link EchoException} for when the input command is unrecognized.
 */
public class UnknownCommandException extends EchoException {
    /** Creates the error shown when the input command is unrecognized. */
    public UnknownCommandException() {
        super("Unknown command. I'm sorry, but I don't know what that means :(");
    }
}
