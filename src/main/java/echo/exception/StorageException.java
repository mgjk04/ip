package echo.exception;

/**
 * An {@link EchoException} for storage related exceptions.
 */
public class StorageException extends EchoException {
    /**
     * Creates a {@link StorageException}.
     * @param message detail of what went wrong, shown to the user
     */
    public StorageException(String message) {
        super("Storage exception occurred. " + message);
    }
}
