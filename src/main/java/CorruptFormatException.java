public class CorruptFormatException extends StorageException {
    public CorruptFormatException(String saveFormat) {
        super("Save file is corrupted. I could not understand this line: " + saveFormat);
    }
}
