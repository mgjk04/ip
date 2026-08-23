package echo.exception;

public class FindFormatException extends EchoException {
    public FindFormatException() {
        super("Invalid find command. Format: find <keyword>");
    }
}
