package echo.exception;

/**
 *  * A {@link EchoException} that signals that the find command was entered without a keyword.
 */
public class FindFormatException extends EchoException {
    public FindFormatException() {
        super("Invalid find command. Format: find <keyword>");
    }
}
