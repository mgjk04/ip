public class StorageException extends EchoException {
    public StorageException(String message) {
        super("Storage exception occurred. " + message);
    }
}
