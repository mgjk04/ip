public class EventFormatException extends EchoException {
    public EventFormatException() {
        super("Invalid event command. Format: event <description> /from <startTime> /to <endTime>");
    }
}
