package echo.exception;

/**
 * Signals that the stats command was entered with unsupported arguments.
 */
public class StatsFormatException extends EchoException {
    /** Creates the error shown when a {@code stats} command has additional arguments */
    public StatsFormatException() {
        super("Invalid stats command. Format: stats");
    }
}
