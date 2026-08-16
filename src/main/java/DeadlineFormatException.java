public class DeadlineFormatException extends EchoException {
    public DeadlineFormatException(){
        super("Invalid deadline command. Format: deadline <description> /by <dueDate>");
    }
}
