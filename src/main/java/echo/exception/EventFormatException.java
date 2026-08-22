package echo.exception;

public class EventFormatException extends EchoException {
    public EventFormatException() {
        super("Invalid event command or dates. Format: event <description> /from <yyyy-MM-dd HHmm> /to <yyyy-MM-dd HHmm>, e.g., event party /from 2019-12-02 1800 /to 2019-12-02 2100");
    }
}
