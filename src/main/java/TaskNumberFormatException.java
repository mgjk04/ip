public class TaskNumberFormatException extends EchoException {
    public TaskNumberFormatException(CommandType command) {
        String keyword = command.getKeyword();
        super("Invalid " + keyword + " command. Format: " + keyword + " <taskNumber>");
    }
}
