public class TaskNumberFormatException extends EchoException {
    public TaskNumberFormatException(CommandType command) {
        super("Invalid " + command.name() + " command. Format: " + command + " <taskNumber>");
    }
}
