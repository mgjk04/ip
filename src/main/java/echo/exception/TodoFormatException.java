package echo.exception;

public class TodoFormatException extends EchoException {
    public TodoFormatException() {
        super("Invalid todo command. Format: todo <description>");
    }
}
