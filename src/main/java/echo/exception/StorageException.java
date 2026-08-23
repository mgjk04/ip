package echo.exception;

/**
 * Signals that reading or writing the save file failed, or that its
 * contents are corrupted.
 */
public class StorageException extends EchoException {
    /**
     * Creates an error describing the storage problem.
     *
     * @param message explanation of what went wrong
     */
    public StorageException(String message) {
        super("Storage exception occurred. " + message);
    }
}
