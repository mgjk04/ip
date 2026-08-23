package echo.exception;

/**
 * An {@link StorageException} for when the saved stored data does not match the accepted format.
 */
public class CorruptFormatException extends StorageException {
    /**
     * Creates a {@link CorruptFormatException}.
     * @param saveFormat the offending improperly formatted saved format.
     */
    public CorruptFormatException(String saveFormat) {
        super("Save file is corrupted. I could not understand this line: " + saveFormat);
    }
}
