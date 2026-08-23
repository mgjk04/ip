package echo.exception;

/**
 * Signals that a save-file line cannot be understood because it is missing
 * fields, has empty values, or names an unknown task type.
 */
public class CorruptFormatException extends StorageException {
    /**
     * Creates an error that quotes the unreadable save-file line.
     *
     * @param saveFormat the offending line from the save file
     */
    public CorruptFormatException(String saveFormat) {
        super("Save file is corrupted. I could not understand this line: " + saveFormat);
    }
}
