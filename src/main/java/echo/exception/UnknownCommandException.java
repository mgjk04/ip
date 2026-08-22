package echo.exception;

public class UnknownCommandException extends EchoException {
    public UnknownCommandException() {
        super("Unknown command. I'm sorry, but I don't know what that means :(");
    }
}
