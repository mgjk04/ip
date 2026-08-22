public class TaskNumberFormatException extends EchoException {
    public TaskNumberFormatException(String keyword) {
        super("Invalid " + keyword + " command. Format: " + keyword + " <taskNumber>");
    }
}
