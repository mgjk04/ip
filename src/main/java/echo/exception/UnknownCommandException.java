package echo.exception;

/**
 * Signals that the user entered a command keyword the chatbot does not know.
 */
public class UnknownCommandException extends EchoException {
    /**
     * Creates an error telling the user the command was not understood.
     */
    public UnknownCommandException() {
        super("Unknown command. I'm sorry, but I don't know what that means :(");
    }
}
