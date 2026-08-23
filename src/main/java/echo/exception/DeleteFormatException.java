package echo.exception;

/**
 * A {@link EchoException}
 */
public class DeleteFormatException extends EchoException {
    /** Creates the error shown when a delete command lacks a valid task number. */
    public DeleteFormatException() {
        super("Invalid delete command. Format: delete <taskNumber>");
    }
}
